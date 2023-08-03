package com.smile.model.service

import com.smile.model.Contact
import com.smile.model.User

interface StorageService {
    suspend fun saveUser(user: User)
    suspend fun saveContact(contact: Contact)
}