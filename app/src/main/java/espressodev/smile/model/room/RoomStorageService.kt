package espressodev.smile.model.room

import espressodev.smile.model.room.module.SmileDao
import javax.inject.Inject


class RoomStorageService @Inject constructor(
    private val smileDao: SmileDao,
) {
    fun getContacts() = smileDao.getContacts()
    suspend fun insertContact(contact: ContactEntity) =
        smileDao.insertBothContacts(contact)

    fun getContact(contactId: String) = smileDao.getContact(contactId)

    suspend fun updateContactLastMessage(contactId: String, lastMessage: String, lastMessageTimeStamp: Long) =
        smileDao.updateContactLastMessage(contactId, lastMessage, lastMessageTimeStamp)

    suspend fun insertSearchHistoryQuery(searchHistoryQuery: SearchHistoryQueryEntity) =
        smileDao.insertSearchHistoryQuery(searchHistoryQuery)

    fun getSearchHistoryQuery() = smileDao.getSearchHistoryQuery()

    suspend fun deleteContact(contactId: String) = smileDao.deleteContact(contactId)

    fun isContactExist(contactId: String) = smileDao.isContactExist(contactId)

    suspend fun updateContact(contactId: String, firstName: String, lastName: String, roomId: String) =
        smileDao.updateContact(contactId, firstName, lastName, roomId)
}