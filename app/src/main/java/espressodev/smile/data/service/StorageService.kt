package espressodev.smile.data.service

import espressodev.smile.data.service.model.Contact
import espressodev.smile.data.service.model.Message
import espressodev.smile.data.service.model.Room
import espressodev.smile.data.service.model.User
import espressodev.smile.data.room.ContactEntity
import espressodev.smile.data.service.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val user: Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun updateUserEmailVerification()
    suspend fun saveContact(scope: CoroutineScope, firstContact: Contact, secondContact: Contact)

    suspend fun updateUserName(name: String): Response<Boolean>

    suspend fun findIdByEmail(email: String): Response<String>

    suspend fun getContacts(): Flow<List<Contact>>

    suspend fun getContact(contactId: String): Flow<Contact>

    suspend fun sendMessage(
        message: Message,
        roomId: String,
        contactId: String
    )

    suspend fun getMessages(
        roomId: String,
        contactId: String
    ): Flow<List<Message>>

    suspend fun saveFcmToken(token: String)

    suspend fun getUser(): User?

    fun getNonEmptyMessageRooms(
        roomIds: List<String>,
        onDataChange: (List<Room>) -> Unit
    ): Response<Boolean>

    suspend fun deleteUser()
}