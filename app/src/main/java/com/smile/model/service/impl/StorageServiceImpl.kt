package com.smile.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.smile.model.Contact
import com.smile.model.service.AccountService
import com.smile.model.service.StorageService
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
): StorageService {
    override suspend fun saveContact(contact: Contact) {

    }


    companion object {
        private const val CONTACT_COLLECTION = "contacts"

    }
}