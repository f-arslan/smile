package com.smile.model.service.impl

import android.util.Log
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
import com.smile.model.notification.models.NotificationData
import com.smile.model.notification.models.PushNotification
import com.smile.model.notification.models.RetrofitObject
import com.smile.model.room.ContactEntity
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
                    Room(
                        createdAt = currentTime,
                        contacts = listOf(firstContact, secondContact),
                    )
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
        val batch = firestore.batch()
        val messageRef = roomColRef.document(roomId).collection(MESSAGE_COLLECTION).document()
        batch.set(messageRef, message)
        batch.update(roomColRef.document(roomId), CONTACT_LAST_MESSAGE, message)
        scope.launch(Dispatchers.IO) {
            roomStorageService.updateContactLastMessage(
                contactId,
                message.content,
                message.timestamp
            )
        }
        batch.commit().await()
        sendPushNotification(message, contactId, roomId)
    }

    private suspend fun sendPushNotification(message: Message, contactId: String, roomId: String) {
        val friendId = contactId.split("_")[1]
        val userDoc = getUserDocRef(friendId).get().await().toObject<User>()
            ?: throw Exception("User not found")
        if (userDoc.fcmToken.isEmpty()) throw Exception("User has no fcm token")
        val data = NotificationData(
            token = userDoc.fcmToken,
            title = userDoc.displayName,
            body = message.content,
            roomId = roomId,
            contactId = contactId
        )
        val pushNotification = PushNotification(to = userDoc.fcmToken, data = data)
        val response = RetrofitObject.notificationApi?.postNotification(pushNotification)
        if (response != null && response.isSuccessful) {
            Log.d("StorageServiceImpl", "Notification sent successfully")
        } else {
            throw Exception("Retrofit error: ${response?.errorBody()?.string()}")
        }
    }

    override suspend fun getMessages(
        scope: CoroutineScope,
        roomId: String,
        contactId: String
    ): Flow<List<Message>> {
        // Listen changes in room collection if there is update in last message update the db
        roomColRef.document(roomId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("StorageServiceImpl", "Error while listening to room collection", error)
                return@addSnapshotListener
            }
            value ?: throw Exception("Room not found")
            val room = value.toObject<Room>() ?: throw Exception("Room not found")
            scope.launch(Dispatchers.IO) {
                roomStorageService.updateContactLastMessage(
                    contactId,
                    room.lastMessage.content,
                    room.lastMessage.timestamp
                )
            }
        }

        return roomColRef.document(roomId).collection(
            MESSAGE_COLLECTION
        ).orderBy(MESSAGE_TIMESTAMP, Query.Direction.DESCENDING).snapshots().map {
            it.toObjects<Message>()
        }
    }


    override suspend fun saveFcmToken(token: String) {
        getUserDocRef(auth.currentUserId).update(USER_FCM_TOKEN, token).await()
    }

    override suspend fun getUser() =
        getUserDocRef(auth.currentUserId).get().await().toObject<User>()

    override fun getNonEmptyMessageRooms(userId: String): Flow<List<Room>> {
        return roomColRef.whereArrayContains(ROOM_USER_IDS, userId).snapshots().map {
            it.toObjects<Room>()
        }
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
        private const val USER_FCM_TOKEN = "fcmToken"
        private const val ROOM_USER_IDS = "userIds"
    }
}