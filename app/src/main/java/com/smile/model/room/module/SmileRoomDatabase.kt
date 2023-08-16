package com.smile.model.room.module

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smile.model.room.ContactEntity
import com.smile.model.room.MessageEntity
import com.smile.model.room.RoomEntity
import com.smile.model.room.SearchHistoryQueryEntity
import com.smile.model.room.UserEntity

@Database(
    entities = [ContactEntity::class, UserEntity::class, RoomEntity::class, MessageEntity::class, SearchHistoryQueryEntity::class],
    version = 1,
)
abstract class SmileRoomDatabase : RoomDatabase() {
    abstract fun smileDao(): SmileDao
}