package com.smile.model.service.impl

import android.util.Log
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
            .update(EMAIL_VERIFIED, true).await()
    }

    override suspend fun saveContact(firstContact: Contact, secondContact: Contact) {
        // Create transaction to save both contacts
        firestore.runTransaction { transaction ->
            // Append userId to user's contactIds
            val user1Snapshot =
                transaction.get(firestore.collection(USER_COLLECTION).document(firstContact.userId))
            val user2Snapshot =
                transaction.get(
                    firestore.collection(USER_COLLECTION).document(secondContact.userId)
                )

            val existingIds1 = user1Snapshot.get(USER_CONTACT_IDS_FIELD) as List<*>
            val existingIds2 = user2Snapshot.get(USER_CONTACT_IDS_FIELD) as List<*>
            val firstContactId = firstContact.userId + "_" + firstContact.contactUserId
            val secondContactId = secondContact.userId + "_" + secondContact.contactUserId

            if (!existingIds1.contains(firstContactId)) {
                transaction.update(
                    firestore.collection(USER_COLLECTION).document(firstContact.userId),
                    USER_CONTACT_IDS_FIELD,
                    existingIds1 + firstContactId
                )
            }

            if (!existingIds2.contains(secondContactId)) {
                transaction.update(
                    firestore.collection(USER_COLLECTION).document(secondContact.userId),
                    USER_CONTACT_IDS_FIELD,
                    existingIds2 + secondContactId
                )
            }

            // Save contacts
            if (!existingIds1.contains(firstContactId)) {
                transaction.set(
                    firestore.collection(CONTACT_COLLECTION).document(firstContactId),
                    firstContact
                )
            }

            if (!existingIds2.contains(secondContactId)) {
                transaction.set(
                    firestore.collection(CONTACT_COLLECTION).document(secondContactId),
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

            onDataChange(contacts.groupBy { it.contactUserId }.values.toList())
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
    }

    override suspend fun getMessages(recipientId: String, onDataChange: (List<Message>) -> Unit) {
        val query = messageColRef
            .whereEqualTo(SENDER_ID, auth.currentUserId)
            .whereEqualTo(RECIPIENT_ID, recipientId)
            .orderBy(TIMESTAMP, Query.Direction.ASCENDING)
        query.addSnapshotListener { snapshot, _ ->
            val messages = snapshot?.toObjects(Message::class.java)
            onDataChange(messages?.toList() ?: emptyList())
        }

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
        private const val USER_ID = "userId"
        private const val CONTACT_ID = "contactId"
        private const val EMAIL_VERIFIED = "emailVerified"
        private const val SENDER_ID = "senderId"
        private const val RECIPIENT_ID = "recipientId"
        private const val TIMESTAMP = "timestamp"
    }
}