package com.smile.model.room.module

import android.content.Context
import androidx.room.Room
import com.smile.util.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SmileRoomDatabase =
        Room.databaseBuilder(
            context,
            SmileRoomDatabase::class.java,
            DB_NAME
        ).build()

    @Singleton
    @Provides
    fun provideSmileDao(db: SmileRoomDatabase) = db.smileDao()
}
