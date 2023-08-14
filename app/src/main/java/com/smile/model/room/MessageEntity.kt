package com.smile.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smile.util.Constants.DB_TABLE_MESSAGE

@Entity(tableName = DB_TABLE_MESSAGE)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val messageId: String = "",
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = 0L
)