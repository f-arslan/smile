package com.smile.model.room.module

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smile.model.room.RoomContact
import com.smile.model.room.RoomUser

@Database(entities = [RoomContact::class, RoomUser::class], version = 1)
abstract class SmileRoomDatabase: RoomDatabase() {
    abstract fun smileDao(): SmileDao
}