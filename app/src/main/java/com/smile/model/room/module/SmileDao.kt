package com.smile.model.room.module

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.smile.model.room.RoomContact
import kotlinx.coroutines.flow.Flow

@Dao
interface SmileDao {
    @Insert
    suspend fun insertBothContacts(contact: RoomContact)
    @Query("SELECT * FROM contacts")
    fun getContacts(): Flow<List<RoomContact>>
}