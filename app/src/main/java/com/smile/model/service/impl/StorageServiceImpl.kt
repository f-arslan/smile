package com.smile.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.smile.model.Contact
import com.smile.model.User
import com.smile.model.service.AccountService
import com.smile.model.service.StorageService
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
): StorageService {
    override suspend fun saveUser(user: User) {
        firestore.collection(USER_COLLECTION).document(auth.currentUserId).set(user)
    }

    override suspend fun saveContact(contact: Contact) {

    }


    companion object {
        private const val CONTACT_COLLECTION = "contacts"
        private const val USER_COLLECTION = "users"

    }
}