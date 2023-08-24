package com.smile.model.room.module

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smile.model.room.ContactEntity
import com.smile.model.room.SearchHistoryQueryEntity

@Database(
    entities = [ContactEntity::class,  SearchHistoryQueryEntity::class],
    version = 1,
)
abstract class SmileRoomDatabase : RoomDatabase() {
    abstract fun smileDao(): SmileDao
}