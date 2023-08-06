package com.smile.model.service.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.smile.model.Contact
import com.smile.model.User
import com.smile.model.service.AccountService
import com.smile.model.service.StorageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {
    override val user: Flow<User?>
        get() = firestore.collection(USER_COLLECTION).document(auth.currentUserId).dataObjects()

    override val contacts: Flow<List<Contact>>
        get() = TODO()

    override suspend fun saveUser(user: User) {
        firestore.collection(USER_COLLECTION).document(auth.currentUserId).set(user).await()
    }

    override suspend fun updateUserEmailVerification() {
        firestore.collection(USER_COLLECTION).document(auth.currentUserId)
            .update("emailVerified", true).await()
    }

    override suspend fun saveContact(firstContact: Contact, secondContact: Contact) {
        // Create transaction to save both contacts
        firestore.runTransaction { transaction ->
            // Append userId to user's contactIds
            val user1Snapshot =
                transaction.get(firestore.collection(USER_COLLECTION).document(firstContact.userId))
            val user2Snapshot =
                transaction.get(firestore.collection(USER_COLLECTION).document(secondContact.userId))

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
        coroutineScope: CoroutineScope,
        onDataChange: (List<List<Contact>>) -> Unit
    ) {
        firestore.runTransaction { transition ->
            val userRef = transition.get(getUserDocRef(auth.currentUserId))
            val contactIds = userRef.get(USER_CONTACT_IDS_FIELD) as List<*>
            val contacts = mutableListOf<Contact>()
            contactIds.forEach { contactId ->
                val contactRef = transition.get(getContactDocRef(contactId as String))
                val contact = contactRef.toObject(Contact::class.java)
                if (contact != null)
                    contacts.add(contact)
            }
            // Sort contact by their firstName + lastName
            val sortedContacts = contacts.sortedBy { it.firstName + it.lastName }
            // Group them by their first letter
            val groupedContacts = sortedContacts.groupBy { it.firstName.first() }
            // Convert to list of list
            val groupedContactsList = groupedContacts.map { it.value }
            onDataChange(groupedContactsList)
        }
    }

    private fun getUserCollRef() = firestore.collection(USER_COLLECTION)
    private fun getUserDocRef(id: String) = getUserCollRef().document(id)

    private fun getContactCollRef() = firestore.collection(CONTACT_COLLECTION)
    private fun getContactDocRef(id: String) = getContactCollRef().document(id)

    companion object {
        private const val CONTACT_COLLECTION = "contacts"
        private const val USER_COLLECTION = "users"
        private const val USER_CONTACT_IDS_FIELD = "contactIds"
        private const val USER_ID = "userId"
        private const val CONTACT_ID = "contactId"

    }
}