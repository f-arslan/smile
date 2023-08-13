package com.smile.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smile.util.Constants.DB_TABLE_USER


@Entity(tableName = DB_TABLE_USER)
data class RoomUser(
    @PrimaryKey(autoGenerate = true) val id: Int = -1,
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val isEmailVerified : Boolean = false,
    val contactIds: List<String> = emptyList(),
)