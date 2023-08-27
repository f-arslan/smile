package com.smile.ui.view_models

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.smile.SmileViewModel
import com.smile.model.User
import com.smile.model.room.ContactEntity
import com.smile.model.room.RoomStorageService
import com.smile.model.room.SearchHistoryQueryEntity
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import com.smile.util.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val roomStorageService: RoomStorageService,
    private val storageService: StorageService,
    private val accountService: AccountService,
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

            val filteredContacts = if (searchQuery.length >= 3) {
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
        getRooms()
        getCurrentUser()
        getSearchHistoryQueries()
        saveFcmToken()
    }

    private fun getRooms() {
        launchCatching {
            storageService.getNonEmptyMessageRooms(accountService.currentUserId).collect {
                val nonEmptyRooms = it.filter { room -> room.lastMessage.content.isNotEmpty() }
                val contactEntities = mutableListOf<ContactEntity>()
                for (room in nonEmptyRooms) {
                    val friendContact =
                        room.contacts.filter { it.contactId != accountService.currentUserId }[0]
                    val contactEntity = friendContact.toRoomContact().copy(
                        lastMessage = room.lastMessage.content,
                        lastMessageTimeStamp = room.lastMessage.timestamp
                    )
                    contactEntities.add(contactEntity)
                }
                _contacts.value = Response.Success(contactEntities)
            }
        }
    }

    private fun getCurrentUser() {
        launchCatching {
            storageService.user.collect {
                if (it != null) {
                    _user.value = Response.Success(it)
                }
            }
        }
    }

    private fun saveFcmToken() {
        launchCatching {
            val currentUser = storageService.getUser()
            if (currentUser != null && currentUser.fcmToken.isEmpty()) {
                val token = Firebase.messaging.token.await()
                storageService.saveFcmToken(token)
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