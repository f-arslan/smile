package com.smile.model.service.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.smile.model.Contact
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
            .update("emailVerified", true).await()
    }

    override suspend fun saveContact(firstContact: Contact, secondContact: Contact) {
        // Create transaction to save both contacts
        firestore.runTransaction { transition ->
            // Append userId to user's contactIds
            val user1Snapshot =
                transition.get(firestore.collection(USER_COLLECTION).document(firstContact.userId))
            val user2Snapshot =
                transition.get(firestore.collection(USER_COLLECTION).document(secondContact.userId))

            val existingIds1 = user1Snapshot.get(USER_CONTACT_IDS_FIELD) as List<*>
            val existingIds2 = user2Snapshot.get(USER_CONTACT_IDS_FIELD) as List<*>
            // add contactId to user's contactIds
            val updatedIds1 = existingIds1 + firstContact.contactUserId
            val updatedIds2 = existingIds2 + secondContact.contactUserId

            if (!existingIds1.contains(firstContact.contactUserId)) {
                Log.d("StorageService", "firstContact.contactUserId: ${firstContact.contactUserId}")
                transition.update(
                    firestore.collection(USER_COLLECTION).document(firstContact.userId),
                    USER_CONTACT_IDS_FIELD,
                    updatedIds1
                )
            }

            if (!existingIds2.contains(secondContact.contactUserId)) {
                transition.update(
                    firestore.collection(USER_COLLECTION).document(secondContact.userId),
                    USER_CONTACT_IDS_FIELD,
                    updatedIds2
                )
            }

            if (!existingIds1.contains(firstContact.contactUserId)) {
                transition.set(
                    firestore.collection(CONTACT_COLLECTION).document(),
                    firstContact
                )
            }

            if (!existingIds2.contains(secondContact.contactUserId)) {
                transition.set(
                    firestore.collection(CONTACT_COLLECTION).document(),
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


    companion object {
        private const val CONTACT_COLLECTION = "contacts"
        private const val USER_COLLECTION = "users"
        private const val USER_CONTACT_IDS_FIELD = "contactIds"

    }
}