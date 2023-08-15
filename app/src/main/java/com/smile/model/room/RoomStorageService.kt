package com.smile.model.room

import com.smile.model.room.module.SmileDao
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


class RoomStorageService @Inject constructor(
    private val smileDao: SmileDao,
) {
    fun getContacts() = smileDao.getContacts()
    suspend fun insertContact(contact: ContactEntity) =
        smileDao.insertBothContacts(contact)

    fun getContact(contactId: String) = smileDao.getContact(contactId)
    suspend fun insertRoom(room: RoomEntity) = smileDao.insertRoom(room)
    suspend fun updateRoomLastMessage(roomId: String, lastMessageId: Int) =
        smileDao.updateRoomLastMessage(roomId, lastMessageId)

    suspend fun updateContactLastMessage(contactId: String, lastMessageId: Int) =
        smileDao.updateContactLastMessage(contactId, lastMessageId)

    suspend fun insertMessage(message: MessageEntity): Long {
        return smileDao.insertMessage(message)
    }
    fun getContactsWithNonEmptyLastMessageId() = smileDao.getContactsWithLastMessage()

}