package espressodev.smile.data.service.impl

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import espressodev.smile.data.service.model.Contact
import espressodev.smile.data.service.model.Message
import espressodev.smile.data.service.model.Room
import espressodev.smile.data.service.model.User
import espressodev.smile.data.notification.model.NotificationData
import espressodev.smile.data.notification.model.PushNotification
import espressodev.smile.data.notification.module.RetrofitObject
import espressodev.smile.data.room.ContactEntity
import espressodev.smile.data.room.RoomStorageService
import espressodev.smile.data.service.AccountService
import espressodev.smile.data.service.StorageService
import espressodev.smile.data.service.model.Response
import espressodev.smile.domain.util.getCurrentTimestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val roomStorageService: RoomStorageService,
    private val accountService: AccountService
) : StorageService {
    override val user: Flow<User?>
        get() = firestore.collection(USER_COLLECTION).document(accountService.currentUserId)
            .dataObjects()

    override suspend fun saveUser(user: User) {
        firestore.collection(USER_COLLECTION).document(accountService.currentUserId).set(user)
            .await()
    }

    override suspend fun updateUserEmailVerification() {
        val user = getUserDocRef(accountService.currentUserId).get().await().toObject<User>()
            ?: throw Exception("User not found")
        if (user.isEmailVerified) {
            return
        }
        getUserDocRef(accountService.currentUserId)
            .update(USER_EMAIL_VERIFIED, true).await()
    }

    override suspend fun saveContact(
        scope: CoroutineScope,
        firstContact: Contact,
        secondContact: Contact
    ) {
        val firstContactId = firstContact.userId + "_" + firstContact.friendId
        val secondContactId = secondContact.userId + "_" + secondContact.friendId
        scope.launch(Dispatchers.IO) {
            val user1Task = async { getUserDocRef(firstContact.userId) }
            val user2Task = async { getUserDocRef(secondContact.userId) }
            val user1Doc = user1Task.await()
            val user2Doc = user2Task.await()
            val user1 = user1Doc.get().await().toObject<User>()
                ?: throw Exception("User not found".withTag())
            val user2 = user2Doc.get().await().toObject<User>()
                ?: throw Exception("User not found".withTag())
            val batch = firestore.batch()
            var roomId = ""
            if (!user1.contactIds.contains(firstContactId) && !user2.contactIds.contains(
                    secondContactId
                )
            ) {
                val currentTime = getCurrentTimestamp()
                val roomRef = async { roomColRef.document() }.await().also { roomId = it.id }
                batch.set(
                    roomRef,
                    Room(
                        roomId = roomId,
                        createdAt = currentTime,
                        contacts = listOf(
                            firstContact.copy(contactId = firstContactId),
                            secondContact.copy(contactId = secondContactId)
                        ),
                    )
                )
                batch.update(user1Doc, USER_CONTACT_IDS_FIELD, user1.contactIds + firstContactId)
                batch.update(user2Doc, USER_CONTACT_IDS_FIELD, user2.contactIds + secondContactId)
                batch.set(
                    getContactColUnderUser(user1Doc.id).document(firstContactId),
                    firstContact.copy(roomId = roomId, contactId = firstContactId)
                )
                batch.set(
                    getContactColUnderUser(user2Doc.id).document(secondContactId),
                    secondContact.copy(roomId = roomId, contactId = secondContactId)
                )
                if (!user1.roomIds.contains(roomId)) {
                    batch.update(user1Doc, USER_ROOM_IDS, user1.roomIds + roomId)
                }
                if (!user2.roomIds.contains(roomId)) {
                    batch.update(user2Doc, USER_ROOM_IDS, user2.roomIds + roomId)
                }
            }
            batch.commit().await()
            // Save to Room DB for caching
            if (roomId.isEmpty()) {
                Log.e("StorageServiceImpl", "Room id is empty")
                return@launch
            }
            roomStorageService.insertContact(
                firstContact.copy(
                    contactId = firstContactId,
                    roomId = roomId
                ).toRoomContact()
            )
        }
    }


    override suspend fun findIdByEmail(email: String): Response<String> {
        val querySnapshot = firestore.collection(USER_COLLECTION)
            .whereEqualTo(User::email.name, email).get().await()
        val user = querySnapshot.toObjects<User>().firstOrNull()
        return if (user != null) {
            Response.Success(user.userId)
        } else {
            Response.Failure(Exception("User not found"))
        }
    }

    override suspend fun getContacts(
        scope: CoroutineScope,
        onDataChange: (List<ContactEntity>) -> Unit
    ) {
        getUserDocRef(accountService.currentUserId).collection(CONTACT_COLLECTION).snapshots()
            .map { querySnapshot ->
                querySnapshot.documentChanges.mapNotNull { documentChange ->
                    when (documentChange.type) {
                        DocumentChange.Type.ADDED -> {
                            documentChange.document.toObject<ContactEntity>()
                        }

                        DocumentChange.Type.MODIFIED -> {
                            documentChange.document.toObject<ContactEntity>()
                        }

                        DocumentChange.Type.REMOVED -> {
                            documentChange.document.toObject<ContactEntity>()
                        }
                    }
                }
            }.collect {
                onDataChange(it)
            }
    }

    override suspend fun getContact(contactId: String) =
        getContactColUnderUser(accountService.currentUserId).document(contactId)
            .dataObjects<Contact>()
            .mapNotNull { it }

    override suspend fun sendMessage(
        message: Message,
        roomId: String,
        contactId: String
    ) {
        val batch = firestore.batch()
        val messageRef = roomColRef.document(roomId).collection(MESSAGE_COLLECTION).document()
        batch.set(messageRef, message)
        batch.update(roomColRef.document(roomId), CONTACT_LAST_MESSAGE, message)
        batch.commit().await()
        sendPushNotification(message, contactId, roomId)
    }

    private suspend fun sendPushNotification(
        message: Message,
        contactId: String,
        roomId: String
    ): Response<Boolean> {
        val friendId = contactId.split("_")[1]
        val friendUserDoc = getUserDocRef(friendId).get().await().toObject<User>()
            ?: return Response.Failure(Exception("User not found"))
        val currentUserDisplayName = getUserDocRef(accountService.currentUserId).get().await()
            .toObject<User>()?.displayName ?: return Response.Failure(Exception("User not found"))
        if (friendUserDoc.fcmToken.isEmpty())
            return Response.Failure(Exception("Friend fcm token is empty"))

        val data = NotificationData(
            token = friendUserDoc.fcmToken,
            title = currentUserDisplayName,
            body = message.content,
            roomId = roomId,
            contactId = contactId
        )
        val pushNotification = PushNotification(to = friendUserDoc.fcmToken, data = data)
        val response = RetrofitObject.notificationApi?.postNotification(pushNotification)
        return if (response != null && response.isSuccessful) {
            Response.Success(true)
        } else {
            Response.Failure(Exception("Retrofit error: ${response?.errorBody()?.string()}"))
        }
    }

    override suspend fun getMessages(
        roomId: String,
        contactId: String
    ): Flow<List<Message>> {
        return roomColRef.document(roomId).collection(
            MESSAGE_COLLECTION
        ).orderBy(MESSAGE_TIMESTAMP, Query.Direction.DESCENDING).snapshots().map {
            it.toObjects<Message>()
        }
    }


    override suspend fun saveFcmToken(token: String) {
        if (accountService.currentUserId.isEmpty()) {
            Log.e("StorageServiceImpl", "User id is empty")
            return
        }
        getUserDocRef(accountService.currentUserId).update(USER_FCM_TOKEN, token).await()
    }

    override suspend fun updateUserName(name: String): Response<Boolean> {
        if (accountService.currentUserId.isEmpty())
            return Response.Failure(Exception("User id is empty".withTag()))

        val rooms =
            getUserDocRef(accountService.currentUserId).get().await().toObject<User>()?.roomIds
                ?: return Response.Failure(Exception("Room ids is empty".withTag()))

        val batch = firestore.batch()

        rooms.forEach { room ->
            val roomDocRef = roomColRef.document(room)
            val contacts =
                roomDocRef.get().await().toObject<Room>()?.contacts
                    ?: return Response.Failure(Exception("Contacts is empty".withTag()))
            contacts.let {
                it.forEachIndexed { index, contact ->
                    if (accountService.currentUserId == contact.friendId) {
                        val updateContact = contact.copy(firstName = name)
                        val updatedContacts = it.toMutableList()
                        updatedContacts[index] = updateContact
                        batch.update(roomDocRef, ROOM_CONTACTS, updatedContacts)
                        return@forEach
                    }
                }
            }
        }
        batch.update(getUserDocRef(accountService.currentUserId), USER_DISPLAY_NAME, name)
        batch.commit().await()
        return Response.Success(true)
    }

    override suspend fun deleteUser() {
        getUserDocRef(accountService.currentUserId).delete().await()
    }

    override suspend fun getUser() =
        getUserDocRef(accountService.currentUserId).get().await().toObject<User>()

    override fun getNonEmptyMessageRooms(
        roomIds: List<String>,
        onDataChange: (List<Room>) -> Unit
    ): Response<Boolean> {
        if (roomIds.isEmpty()) {
            return Response.Failure(Exception("Room ids is empty".withTag()))
        }
        roomColRef.whereIn(ROOM_ID, roomIds).addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error while listening to room collection", error)
                return@addSnapshotListener
            }
            val rooms = querySnapshot?.toObjects<Room>() ?: emptyList()
            onDataChange(rooms.filter { room -> roomIds.contains(room.roomId) && room.lastMessage.content.isNotEmpty() })
        }
        return Response.Success(true)
    }

    private val roomColRef by lazy { firestore.collection(ROOM_COLLECTION) }
    private val userColRef by lazy { firestore.collection(USER_COLLECTION) }

    private fun getUserDocRef(id: String) = userColRef.document(id)
    private fun getContactColUnderUser(id: String) =
        getUserDocRef(id).collection(CONTACT_COLLECTION)

    companion object {
        private const val CONTACT_COLLECTION = "contacts"
        private const val USER_COLLECTION = "users"
        private const val MESSAGE_COLLECTION = "messages"
        private const val ROOM_COLLECTION = "rooms"

        private const val USER_CONTACT_IDS_FIELD = "contactIds"
        private const val USER_EMAIL_VERIFIED = "emailVerified"
        private const val MESSAGE_TIMESTAMP = "timestamp"
        private const val CONTACT_LAST_MESSAGE = "lastMessage"
        private const val USER_FCM_TOKEN = "fcmToken"
        private const val USER_ROOM_IDS = "roomIds"
        private const val ROOM_ID = "roomId"
        private const val ROOM_CONTACTS = "contacts"
        private const val USER_DISPLAY_NAME = "displayName"

        private const val TAG = "StorageServiceImpl"
        private fun String.withTag() = "$TAG: $this"
    }
}