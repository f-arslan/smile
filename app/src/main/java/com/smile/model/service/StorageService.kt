package com.smile.model.service

import com.smile.model.Contact
import com.smile.model.Message
import com.smile.model.User
import com.smile.model.room.ContactEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val user: Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun updateUserEmailVerification()
    suspend fun saveContact(scope: CoroutineScope, firstContact: Contact, secondContact: Contact)

    suspend fun findIdByEmail(email: String): String?

    suspend fun getContacts(scope: CoroutineScope, onDataChange: (List<List<ContactEntity>>) -> Unit)
    suspend fun getContact(contactId: String): Flow<ContactEntity>

    suspend fun sendMessage(
        scope: CoroutineScope,
        message: Message,
        roomId: String,
        contactId: String
    )

    suspend fun getMessages(roomId: String): Flow<List<Message>>

    suspend fun saveFcmToken(token: String)

    suspend fun getUser(): User?
}