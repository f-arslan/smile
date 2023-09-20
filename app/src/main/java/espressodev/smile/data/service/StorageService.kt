package espressodev.smile.data.service

import espressodev.smile.data.Contact
import espressodev.smile.data.Message
import espressodev.smile.data.Room
import espressodev.smile.data.User
import espressodev.smile.data.room.ContactEntity
import espressodev.smile.data.service.module.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val user: Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun updateUserEmailVerification()
    suspend fun saveContact(scope: CoroutineScope, firstContact: Contact, secondContact: Contact)

    suspend fun updateUserName(name: String): Response<Boolean>

    suspend fun findIdByEmail(email: String): Response<String>

    suspend fun getContacts(
        scope: CoroutineScope,
        onDataChange: (List<ContactEntity>) -> Unit
    )

    suspend fun getContact(contactId: String): Flow<Contact>

    suspend fun sendMessage(
        scope: CoroutineScope,
        message: Message,
        roomId: String,
        contactId: String
    )

    suspend fun getMessages(
        scope: CoroutineScope,
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