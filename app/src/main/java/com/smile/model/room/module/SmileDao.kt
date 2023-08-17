package com.smile.model.room.module

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smile.model.room.ContactEntity
import com.smile.model.room.HomeContactEntity
import com.smile.model.room.MessageEntity
import com.smile.model.room.RoomEntity
import com.smile.model.room.SearchHistoryQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SmileDao {
    @Insert
    suspend fun insertBothContacts(contact: ContactEntity)
    @Query("SELECT * FROM contacts")
    fun getContacts(): Flow<List<ContactEntity>>
    @Query("SELECT * FROM contacts WHERE contactId = :contactId")
    fun getContact(contactId: String): Flow<ContactEntity>
    @Insert
    suspend fun insertRoom(room: RoomEntity)
    @Query("Update rooms SET lastMessageId = :lastMessageId WHERE roomId = :roomId")
    suspend fun updateRoomLastMessage(roomId: String, lastMessageId: Int)

    @Query("Update contacts SET lastMessageId = :lastMessageId WHERE contactId = :contactId")
    suspend fun updateContactLastMessage(contactId: String, lastMessageId: Int)

    @Insert
    suspend fun insertMessage(message: MessageEntity): Long


    @Query("SELECT contacts.id, contacts.contactId, contacts.userId, contacts.friendId, contacts.firstName, " +
            "contacts.lastName, contacts.roomId, messages.content, messages.timestamp FROM contacts " +
            "INNER JOIN messages on contacts.lastMessageId = messages.id ORDER BY messages.timestamp DESC")
    fun getContactsWithLastMessage(): Flow<List<HomeContactEntity>>

    @Insert
    suspend fun insertSearchHistoryQuery(searchHistory: SearchHistoryQueryEntity)

    @Query("SELECT * FROM searchHistoryQueries ORDER BY timestamp DESC LIMIT 5")
    fun getSearchHistoryQuery(): Flow<List<SearchHistoryQueryEntity>>

    @Query("DELETE FROM contacts WHERE contactId = :contactId")
    suspend fun deleteContact(contactId: String)

    @Query("SELECT EXISTS(SELECT * FROM contacts WHERE contactId = :contactId)")
    fun isContactExist(contactId: String): Boolean

    @Query("UPDATE contacts SET firstName = :firstName, lastName = :lastName WHERE contactId = :contactId")
    suspend fun updateContact(contactId: String, firstName: String, lastName: String)
}