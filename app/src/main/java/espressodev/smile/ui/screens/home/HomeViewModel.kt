package espressodev.smile.ui.screens.home

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.data.datastore.DataStoreService
import espressodev.smile.data.datastore.DataStoreService.Companion.IDLE
import espressodev.smile.data.room.ContactEntity
import espressodev.smile.data.room.RoomStorageService
import espressodev.smile.data.room.SearchHistoryQueryEntity
import espressodev.smile.data.service.AccountService
import espressodev.smile.data.service.LogService
import espressodev.smile.data.service.StorageService
import espressodev.smile.data.service.model.Response
import espressodev.smile.data.service.model.User
import espressodev.smile.domain.util.getCurrentTimestamp
import espressodev.smile.ui.screens.profile.notification.notificationRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val roomStorageService: RoomStorageService,
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val dataStoreRepository: DataStoreService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _contacts =
        MutableStateFlow<Response<List<ContactEntity>>>(Response.Loading)
    val contacts = _contacts.asStateFlow()

    private val _searchQueries =
        MutableStateFlow<Response<List<SearchHistoryQueryEntity>>>(Response.Loading)

    private val _searchHistoryQueries =
        MutableStateFlow<Response<List<SearchHistoryQueryEntity>>>(Response.Loading)
    val searchHistoryQueries = _searchHistoryQueries.asStateFlow()

    private val _searchHistoryContacts =
        MutableStateFlow<Response<List<ContactEntity>>>(Response.Loading)
    val searchHistoryContacts = _searchHistoryContacts.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchIsActive = MutableStateFlow(false)
    val searchIsActive = _searchIsActive.asStateFlow()

    private val _user = MutableStateFlow<Response<User>>(Response.Loading)
    val user = _user.asStateFlow()


    fun navigateToNotificationScreen(clearAndNavigate: (String) -> Unit) {
        launchCatching {
            val currentNotificationState = dataStoreRepository.getNotificationsEnabled()
            if (currentNotificationState == IDLE) {
                clearAndNavigate(notificationRoute)
            }
        }
    }

    fun onSearchActiveChange(isActive: Boolean) {
        _searchIsActive.value = isActive
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        val contactsResponse = _contacts.value
        val searchQueriesResponse = _searchQueries.value

        if (contactsResponse is Response.Success && searchQueriesResponse is Response.Success) {
            val searchQuery = _searchQuery.value
            val contacts = contactsResponse.data
            val searchQueries = searchQueriesResponse.data

            val filteredQueries = searchQueries.filter { it.query.contains(searchQuery) }

            val filteredContacts = if (searchQuery.length >= 2) {
                contacts.filter {
                    it.firstName.contains(searchQuery, ignoreCase = true) ||
                            it.lastName.contains(searchQuery, ignoreCase = true)
                }.takeLast(5)
            } else {
                emptyList()  // Clear the list if searchQuery size is smaller than 3
            }

            _searchHistoryQueries.value = Response.Success(filteredQueries)
            _searchHistoryContacts.value = Response.Success(filteredContacts)
        }
    }


    fun onSearchClick(searchQuery: String) {
        launchCatching {
            val contactsResponse = _contacts.value
            val searchQueriesResponse = _searchQueries.value

            if (contactsResponse is Response.Success && searchQueriesResponse is Response.Success) {
                val contactNames = contactsResponse.data.map { "${it.firstName} ${it.lastName}" }
                val queries = searchQueriesResponse.data.map { it.query }.toHashSet()

                if (searchQuery in contactNames && searchQuery !in queries) {
                    val index = contactNames.indexOf(searchQuery)
                    val contactId = contactsResponse.data[index].contactId
                    val roomId = contactsResponse.data[index].roomId
                    insertSearchHistoryQuery(contactId, roomId, searchQuery)
                }
            }
        }
    }

    private fun insertSearchHistoryQuery(contactId: String, roomId: String, searchQuery: String) {
        launchCatching {
            roomStorageService.insertSearchHistoryQuery(
                SearchHistoryQueryEntity(
                    query = searchQuery,
                    timeStamp = getCurrentTimestamp(),
                    contactId = contactId,
                    roomId = roomId
                )
            )
        }
    }


    fun getData() {
        getCurrentUser()
        getSearchHistoryQueries()
        saveFcmToken()
    }


    private fun getRoomContacts(roomIds: List<String>) {
        launchCatching {
            storageService.getNonEmptyMessageRooms(roomIds) {
                val contactEntities = mutableListOf<ContactEntity>()
                it.forEach { room ->
                    val friendContact =
                        room.contacts.firstOrNull { contact -> contact.userId == accountService.currentUserId }
                            ?: return@forEach
                    contactEntities.add(
                        ContactEntity(
                            contactId = accountService.currentUserId + "_" + friendContact.friendId,
                            userId = accountService.currentUserId,
                            friendId = friendContact.userId,
                            firstName = friendContact.firstName,
                            lastName = friendContact.lastName,
                            email = friendContact.email,
                            roomId = room.roomId,
                            lastMessage = room.lastMessage.content,
                            lastMessageTimeStamp = room.lastMessage.timestamp
                        )
                    )
                }
                _contacts.value = Response.Success(contactEntities.toList())
            }
        }
    }

    private fun getCurrentUser() {
        launchCatching {
            storageService.user.collect {
                if (it != null) {
                    _user.value = Response.Success(it)
                    getRoomContacts(it.roomIds)
                }
            }
        }
    }

    private fun saveFcmToken() {
        launchCatching {
            val user = storageService.getUser() ?: return@launchCatching
            val token = Firebase.messaging.token.await()
            val dataStoreFCMToken = dataStoreRepository.getFcmToken()
            if (dataStoreFCMToken == token) {
                return@launchCatching
            }
            if (token != user.fcmToken) {
                storageService.saveFcmToken(token)
            }
            if (dataStoreFCMToken.isBlank()) {
                dataStoreRepository.setFcmToken(token)
            }
        }
    }

    private fun getSearchHistoryQueries() {
        launchCatching {
            roomStorageService.getSearchHistoryQuery().collect {
                _searchQueries.value = Response.Success(it)
            }
        }
    }
}