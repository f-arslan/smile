package espressodev.smile.data.room.module

import android.content.Context
import androidx.room.Room
import espressodev.smile.domain.util.Constants.DB_NAME
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

    @Provides
    fun provideSmileDao(db: SmileRoomDatabase) = db.smileDao()
}
