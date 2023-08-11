package com.smile.model.service.impl

import android.util.Log
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
import com.smile.model.service.AccountService
import com.smile.model.service.StorageService
import com.smile.util.getCurrentTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
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

    override suspend fun saveContact(firstContact: Contact, secondContact: Contact) {
        // Create transaction to save both contacts
        firestore.runTransaction { transaction ->
            val user1Snapshot =
                transaction.get(getUserDocRef(firstContact.userId))
            val user2Snapshot =
                transaction.get(getUserDocRef(secondContact.userId))

            val existingIds1 = user1Snapshot.get(USER_CONTACT_IDS_FIELD) as List<*>
            val existingIds2 = user2Snapshot.get(USER_CONTACT_IDS_FIELD) as List<*>
            val firstContactId = firstContact.userId + "_" + firstContact.friendId
            val secondContactId = secondContact.userId + "_" + secondContact.friendId
            // Create a room collection for both users and add the room id to both contact

            if (!existingIds1.contains(firstContactId)) {
                transaction.update(
                    userColRef.document(firstContact.userId),
                    USER_CONTACT_IDS_FIELD,
                    existingIds1 + firstContactId
                )
                transaction.set(
                    contactColRef.document(firstContactId),
                    firstContact
                )
            }

            if (!existingIds2.contains(secondContactId)) {
                transaction.update(
                    userColRef.document(secondContact.userId),
                    USER_CONTACT_IDS_FIELD,
                    existingIds2 + secondContactId
                )
                transaction.set(
                    contactColRef.document(secondContactId),
                    secondContact
                )
                // Create a room
                var id: String?
                transaction.set(
                    roomColRef.document().also {
                        id = it.id
                    },
                    Room(createdAt = getCurrentTimestamp())
                )
                transaction.update(
                    contactColRef.document(firstContactId),
                    CONTACT_ROOM_ID,
                    id
                )
                transaction.update(
                    contactColRef.document(secondContactId),
                    CONTACT_ROOM_ID,
                    id
                )
            }


            null
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
        onDataChange: (List<List<Contact>>) -> Unit
    ) {
        firestore.runTransaction { transition ->
            val contactIds = transition.get(getUserDocRef(auth.currentUserId))
                .get(USER_CONTACT_IDS_FIELD) as List<String>

            val contacts = contactIds.asSequence()
                .map { transition.get(getContactDocRef(it)).toObject(Contact::class.java) }
                .filterNotNull()

            onDataChange(contacts.groupBy { it.friendId }.values.toList())
        }
    }

    override suspend fun getContact(contactId: String) =
        getContactDocRef(contactId).snapshots().map {
            it.toObject<Contact>()
        }



    override suspend fun sendMessage(message: Message, roomId: String) {
        roomColRef.document(roomId).collection(MESSAGE_COLLECTION).add(message).await()
        roomColRef.document(roomId).update(CONTACT_LAST_MESSAGE, message).await()
    }

    override suspend fun getMessages(roomId: String) = roomColRef.document(roomId).collection(
        MESSAGE_COLLECTION
    ).orderBy(MESSAGE_TIMESTAMP, Query.Direction.DESCENDING).snapshots().map {
        it.toObjects<Message>()
    }


    private val roomColRef by lazy { firestore.collection(ROOM_COLLECTION) }
    private val userColRef by lazy { firestore.collection(USER_COLLECTION) }
    private val contactColRef by lazy { firestore.collection(CONTACT_COLLECTION) }

    private fun getUserDocRef(id: String) = userColRef.document(id)
    private fun getContactDocRef(id: String) = contactColRef.document(id)

    companion object {
        private const val CONTACT_COLLECTION = "contacts"
        private const val USER_COLLECTION = "users"
        private const val MESSAGE_COLLECTION = "messages"
        private const val ROOM_COLLECTION = "rooms"

        private const val USER_CONTACT_IDS_FIELD = "contactIds"
        private const val USER_EMAIL_VERIFIED = "emailVerified"
        private const val MESSAGE_SENDER_ID = "senderId"
        private const val MESSAGE_RECIPIENT_ID = "recipientId"
        private const val MESSAGE_TIMESTAMP = "timestamp"
        private const val CONTACT_LAST_MESSAGE = "lastMessage"
        private const val CONTACT_ROOM_ID = "roomId"
    }
}