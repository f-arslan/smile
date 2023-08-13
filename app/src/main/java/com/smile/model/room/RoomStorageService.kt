package com.smile.model.room

import com.smile.model.room.module.SmileDao
import javax.inject.Inject


class RoomStorageService @Inject constructor(
    private val smileDao: SmileDao
) {
    fun getContact(userId: String) = smileDao.getContact(userId)
    suspend fun insertBothContacts(contact1: RoomContact, contact2: RoomContact) =
        smileDao.insertBothContacts(contact1, contact2)
}