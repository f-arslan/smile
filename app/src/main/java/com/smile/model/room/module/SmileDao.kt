package com.smile.model.room.module

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.smile.model.room.RoomContact
import kotlinx.coroutines.flow.Flow

@Dao
interface SmileDao {
    @Insert
    suspend fun insertBothContacts(contact1: RoomContact, contact2: RoomContact)
    @Query("SELECT * FROM contacts WHERE userId = :userId")
    fun getContact(userId: String): Flow<RoomContact?>
}