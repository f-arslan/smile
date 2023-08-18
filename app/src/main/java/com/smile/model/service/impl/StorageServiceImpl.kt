package com.smile.model.service.impl

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.smile.model.Contact
import com.smile.model.Message
import com.smile.model.Room
import com.smile.model.User
import com.smile.model.room.ContactEntity
import com.smile.model.room.RoomEntity
import com.smile.model.room.RoomStorageService
import com.smile.model.service.AccountService
import com.smile.model.service.StorageService
import com.smile.util.getCurrentTimestamp
import com.smile.util.turnListToGroupByLetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val roomStorageService: RoomStorageService,
    private val auth: AccountService
) : StorageService {
    override val user: Flow<User?>
        get() = firestore.collection(USER_COLLECTION).document(auth.currentUserId).dataObjects()

    override suspend fun saveUser(user: User) {
        firestore.collection(USER_COLLECTION).document(auth.currentUserId).set(user).await()
    }

    override suspend fun updateUserEmailVerification() {
        firestore.collection(USER_COLLECTION).document(auth.currentUserId)
            .update(USER_EMAIL_VERIFIED, true).await()
    }

    override suspend fun saveContact(
        scope: CoroutineScope,
        firstContact: Contact,
        secondContact: Contact
    ) {
        val firstContactId = firstContact.userId + "_" + firstContact.friendId
        val secondContactId = secondContact.userId + "_" + secondContact.friendId
        scope.launch(Dispatchers.IO) {
            val user1Task = async { getUserDocRef(firstContact.userId) }
            val user2Task = async { getUserDocRef(secondContact.userId) }

            val user1Doc = user1Task.await()
            val user2Doc = user2Task.await()

            val user1 = user1Doc.get().await().toObject<User>()
            val user2 = user2Doc.get().await().toObject<User>()

            val batch = firestore.batch()
            if (user1 != null && !user1.contactIds.contains(firstContactId) &&
                user2 != null && !user2.contactIds.contains(secondContactId)
            ) {
                var roomId: String
                val currentTime = getCurrentTimestamp()
                batch.set(
                    roomColRef.document().also { roomId = it.id },
                    Room(createdAt = currentTime)
                )
                batch.update(user1Doc, USER_CONTACT_IDS_FIELD, user1.contactIds + firstContactId)
                batch.update(user2Doc, USER_CONTACT_IDS_FIELD, user2.contactIds + secondContactId)
                batch.set(
                    getContactColUnderUser(user1Doc.id).document(firstContactId),
                    firstContact.copy(roomId = roomId)
                )
                batch.set(
                    getContactColUnderUser(user2Doc.id).document(secondContactId),
                    secondContact.copy(roomId = roomId)
                )
                // Save to Room DB for caching
                roomStorageService.insertContact(
                    firstContact.copy(
                        contactId = firstContactId,
                        roomId = roomId
                    ).toRoomContact()
                )
                roomStorageService.insertRoom(RoomEntity(roomId = roomId, createdAt = currentTime))
            }
            batch.commit().await()
        }
    }


    override suspend fun findIdByEmail(email: String): String? {
        var userId: String? = null
        firestore.collection(USER_COLLECTION)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    userId = it.documents[0].id
                }
            }.await()
        return userId
    }

    override suspend fun getContacts(
        scope: CoroutineScope,
        onDataChange: (List<List<ContactEntity>>) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            val contacts = roomStorageService.getContacts().first()
            // Give user data from Room DB, not updated one
            onDataChange(turnListToGroupByLetter(contacts))

            getContactColUnderUser(auth.currentUserId).snapshots().map { it.documentChanges }
                .collect {
                    // Observe changes for each document
                    it.forEach { documentChange ->
                        val contact = documentChange.document.toObject<Contact>()
                        when (documentChange.type) {
                            DocumentChange.Type.ADDED -> {
                                if (!roomStorageService.isContactExist(contact.contactId))
                                    roomStorageService.insertContact(contact.toRoomContact())
                            }

                            DocumentChange.Type.MODIFIED -> {
                                roomStorageService.updateContact(
                                    contact.contactId,
                                    contact.firstName,
                                    contact.lastName
                                )
                            }

                            DocumentChange.Type.REMOVED -> {
                                roomStorageService.deleteContact(contact.contactId)
                                val userDoc = getUserDocRef(auth.currentUserId).get().await()
                                val user = userDoc.toObject<User>()
                                val contactIds = user?.contactIds ?: emptyList()
                                getUserDocRef(auth.currentUserId).update(
                                    USER_CONTACT_IDS_FIELD,
                                    contactIds.filter { id -> id != contact.contactId }
                                ).await()
                                // TODO: Handle delete for both sides
                            }
                        }
                    }
                    onDataChange(turnListToGroupByLetter(roomStorageService.getContacts().first()))
                }
        }
    }

    override suspend fun getContact(contactId: String) =
        roomStorageService.getContact(contactId)


    override suspend fun sendMessage(
        scope: CoroutineScope,
        message: Message,
        roomId: String,
        contactId: String
    ) {
        val fireStoreMessageId: String
        roomColRef.document(roomId).collection(MESSAGE_COLLECTION).add(message).await().also {
            fireStoreMessageId = it.id
        }
        roomColRef.document(roomId).update(CONTACT_LAST_MESSAGE, message).await()
        scope.launch(Dispatchers.IO) {
            val messageId =
                async {
                    roomStorageService.insertMessage(
                        message.copy(messageId = fireStoreMessageId).toMessageEntity()
                    )
                }.await()
            roomStorageService.updateRoomLastMessage(roomId, messageId.toInt())
            roomStorageService.updateContactLastMessage(contactId, messageId.toInt())
        }
    }

    override suspend fun getMessages(roomId: String) = roomColRef.document(roomId).collection(
        MESSAGE_COLLECTION
    ).orderBy(MESSAGE_TIMESTAMP, Query.Direction.DESCENDING).snapshots().map {
        it.toObjects<Message>()
    }


    private val roomColRef by lazy { firestore.collection(ROOM_COLLECTION) }
    private val userColRef by lazy { firestore.collection(USER_COLLECTION) }

    private fun getUserDocRef(id: String) = userColRef.document(id)
    private fun getContactColUnderUser(id: String) =
        getUserDocRef(id).collection(CONTACT_COLLECTION)

    companion object {
        private const val CONTACT_COLLECTION = "contacts"
        private const val USER_COLLECTION = "users"
        private const val MESSAGE_COLLECTION = "messages"
        private const val ROOM_COLLECTION = "rooms"

        private const val USER_CONTACT_IDS_FIELD = "contactIds"
        private const val USER_EMAIL_VERIFIED = "emailVerified"
        private const val MESSAGE_TIMESTAMP = "timestamp"
        private const val CONTACT_LAST_MESSAGE = "lastMessage"
    }
}