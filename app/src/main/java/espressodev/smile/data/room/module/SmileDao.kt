package espressodev.smile.data.room.module

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import espressodev.smile.data.room.ContactEntity
import espressodev.smile.data.room.SearchHistoryQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SmileDao {
    @Insert
    suspend fun insertBothContacts(contact: ContactEntity)
    @Query("SELECT * FROM contacts")
    fun getContacts(): Flow<List<ContactEntity>>
    @Query("SELECT * FROM contacts WHERE contactId = :contactId")
    fun getContact(contactId: String): Flow<ContactEntity>

    @Insert
    suspend fun insertSearchHistoryQuery(searchHistory: SearchHistoryQueryEntity)

    @Query("SELECT * FROM searchHistoryQueries ORDER BY timestamp DESC LIMIT 5")
    fun getSearchHistoryQuery(): Flow<List<SearchHistoryQueryEntity>>

    @Query("DELETE FROM contacts WHERE contactId = :contactId")
    suspend fun deleteContact(contactId: String)

    @Query("SELECT EXISTS(SELECT * FROM contacts WHERE contactId = :contactId)")
    fun isContactExist(contactId: String): Boolean

    @Query("UPDATE contacts SET firstName = :firstName, lastName = :lastName, roomId = :roomId WHERE contactId = :contactId")
    suspend fun updateContact(contactId: String, firstName: String, lastName: String, roomId: String)
}