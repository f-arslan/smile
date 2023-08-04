package com.smile.model.service.impl

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
            transition.set(
                firestore.collection(CONTACT_COLLECTION).document(firstContact.contactId),
                firstContact
            )
            transition.set(
                firestore.collection(CONTACT_COLLECTION).document(secondContact.contactId),
                secondContact
            )
        }
    }


    override suspend fun findIdByEmail(email: String): String? {
        var userId: String? = null
        firestore.collection(USER_COLLECTION)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    userId = document.id
                    break //exit loop after first match
                }
            }
        return userId
    }


    companion object {
        private const val CONTACT_COLLECTION = "contacts"
        private const val USER_COLLECTION = "users"

    }
}