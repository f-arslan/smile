package com.smile.model.service

import com.smile.model.Contact
import com.smile.model.User
import com.smile.model.service.module.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val user: Flow<User?>
    val contacts: Flow<List<Contact>>
    suspend fun saveUser(user: User)
    suspend fun updateUserEmailVerification()
    suspend fun saveContact(firstContact: Contact, secondContact: Contact)

    suspend fun findIdByEmail(email: String): String?

    suspend fun getContacts(coroutineScope: CoroutineScope, onDataChange: (List<List<Contact>>) -> Unit)
}