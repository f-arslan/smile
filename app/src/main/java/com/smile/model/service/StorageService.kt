package com.smile.model.service

import com.smile.model.Contact

interface StorageService {
    suspend fun saveContact(contact: Contact)
}