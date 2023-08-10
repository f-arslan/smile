package com.smile.model.service

import com.smile.model.Contact
import com.smile.model.Message
import com.smile.model.User
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val user: Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun updateUserEmailVerification()
    suspend fun saveContact(firstContact: Contact, secondContact: Contact)

    suspend fun findIdByEmail(email: String): String?

    suspend fun getContacts(onDataChange: (List<List<Contact>>) -> Unit)
    suspend fun getContact(contactId: String, onDataChange: (Contact) -> Unit)

    suspend fun sendMessage(message: Message)

    suspend fun getMessages(senderId: String, recipientId: String, onDataChange: (List<Message>) -> Unit)
}