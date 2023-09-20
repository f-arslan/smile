package espressodev.smile.data.room.module

import androidx.room.Database
import androidx.room.RoomDatabase
import espressodev.smile.data.room.ContactEntity
import espressodev.smile.data.room.SearchHistoryQueryEntity

@Database(
    entities = [ContactEntity::class,  SearchHistoryQueryEntity::class],
    version = 1,
)
abstract class SmileRoomDatabase : RoomDatabase() {
    abstract fun smileDao(): SmileDao
}