package com.smile.model.room.module

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smile.model.room.ContactEntity
import com.smile.model.room.MessageEntity
import com.smile.model.room.RoomEntity
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
    @Insert
    suspend fun insertMessage(message: MessageEntity): Long
}