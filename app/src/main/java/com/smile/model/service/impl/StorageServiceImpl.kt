package com.smile.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import com.smile.model.Contact
import com.smile.model.Message
import com.smile.model.User
import com.smile.model.service.AccountService
import com.smile.model.service.StorageService
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getContact(contactId: String, onDataChange: (Contact) -> Unit) {
        val contactRef = getContactDocRef(contactId)
        // Listen changes in real time
        contactRef.addSnapshotListener { snapshot, _ ->
            snapshot?.toObject(Contact::class.java)?.let {
                onDataChange(it)
            }
        }
    }

    override suspend fun sendMessage(message: Message) {
        messageColRef.add(message).await()
        val firstContact = message.senderId + "_" + message.recipientId
        val secondContact = message.recipientId + "_" + message.senderId
        getContactDocRef(firstContact).update(CONTACT_LAST_MESSAGE, message)
        getContactDocRef(secondContact).update(CONTACT_LAST_MESSAGE, message)
    }

    override suspend fun getMessages(senderId: String, recipientId: String, onDataChange: (List<Message>) -> Unit){
        val query1 = messageColRef
            .whereEqualTo(MESSAGE_SENDER_ID, senderId)
            .whereEqualTo(MESSAGE_RECIPIENT_ID, recipientId)
            .orderBy(MESSAGE_TIMESTAMP, Query.Direction.ASCENDING)

        val query2 = messageColRef
            .whereEqualTo(MESSAGE_SENDER_ID, recipientId)
            .whereEqualTo(MESSAGE_RECIPIENT_ID, senderId)
            .orderBy(MESSAGE_TIMESTAMP, Query.Direction.ASCENDING)

        val messages = mutableListOf<Message>()
        query1.get().await().documents.forEach {
            messages.add(it.toObject(Message::class.java)!!)
        }
        query2.get().await().documents.forEach {
            messages.add(it.toObject(Message::class.java)!!)
        }
        onDataChange(messages)
        
    }


    private val messageColRef by lazy { firestore.collection(MESSAGE_COLLECTION) }
    private val userColRef by lazy { firestore.collection(USER_COLLECTION) }
    private val contactColRef by lazy { firestore.collection(CONTACT_COLLECTION) }

    private fun getUserDocRef(id: String) = userColRef.document(id)
    private fun getContactDocRef(id: String) = contactColRef.document(id)

    companion object {
        private const val CONTACT_COLLECTION = "contacts"
        private const val USER_COLLECTION = "users"
        private const val MESSAGE_COLLECTION = "messages"

        private const val USER_CONTACT_IDS_FIELD = "contactIds"
        private const val USER_EMAIL_VERIFIED = "emailVerified"
        private const val MESSAGE_SENDER_ID = "senderId"
        private const val MESSAGE_RECIPIENT_ID = "recipientId"
        private const val MESSAGE_TIMESTAMP = "timestamp"
        private const val CONTACT_LAST_MESSAGE = "lastMessage"
    }
}