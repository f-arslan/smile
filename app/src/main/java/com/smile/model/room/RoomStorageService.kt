package com.smile.model.room

import com.smile.model.room.module.SmileDao
import javax.inject.Inject


class RoomStorageService @Inject constructor(
    private val smileDao: SmileDao
) {
    fun getContacts() = smileDao.getContacts()
    suspend fun insertContact(contact: RoomContact) =
        smileDao.insertBothContacts(contact)
}