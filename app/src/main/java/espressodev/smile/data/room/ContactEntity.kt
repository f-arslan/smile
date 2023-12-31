package espressodev.smile.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import espressodev.smile.domain.util.Constants.DB_TABLE_CONTACT


@Entity(tableName = DB_TABLE_CONTACT)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contactId: String = "",
    val userId: String = "",
    val friendId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val roomId: String = "",
    val lastMessage: String = "",
    val lastMessageTimeStamp: Long = 0L,
) {
    fun getFullName() = "$firstName $lastName"
}
