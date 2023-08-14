package com.smile.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smile.model.Message
import com.smile.util.Constants.DB_TABLE_ROOM

@Entity(tableName = DB_TABLE_ROOM)
data class RoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roomId: String = "",
    val createdAt: Long = 0L,
    val lastMessageId: String = ""
)
