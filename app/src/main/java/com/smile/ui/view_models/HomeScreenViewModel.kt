package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.model.User
import com.smile.model.room.HomeContactEntity
import com.smile.model.room.RoomStorageService
import com.smile.model.room.SearchHistoryQueryEntity
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import com.smile.util.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val roomStorageService: RoomStorageService,
    private val storageService: StorageService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _contacts =
        MutableStateFlow<Response<List<HomeContactEntity>>>(Response.Loading)
    val contacts = _contacts.asStateFlow()

    private val _searchQueries =
        MutableStateFlow<Response<List<SearchHistoryQueryEntity>>>(Response.Loading)

    private val _searchHistoryQueries =
        MutableStateFlow<Response<List<SearchHistoryQueryEntity>>>(Response.Loading)
    val searchHistoryQueries = _searchHistoryQueries.asStateFlow()

    private val _searchHistoryContacts =
        MutableStateFlow<Response<List<HomeContactEntity>>>(Response.Loading)
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
        if (_contacts.value is Response.Success && _searchQueries.value is Response.Success) {
            // Get query than update _searchHistoryQueries and _searchHistoryContacts
            val searchQuery = _searchQuery.value
            val contacts = (_contacts.value as Response.Success).data
            val searchQueries = (_searchQueries.value as Response.Success).data
            val filteredQueries = searchQueries.filter { it.query.contains(searchQuery) }
            _searchHistoryQueries.value = Response.Success(filteredQueries)
            _searchHistoryContacts.value = Response.Success(
                contacts.filter {
                    it.firstName.contains(searchQuery) || it.lastName.contains(
                        searchQuery
                    )
                }.takeLast(5)
            )
        }
    }

    fun onSearchClick(searchQuery: String) {
        launchCatching {
            when (val res = _searchQueries.value) {
                is Response.Success -> {
                    val queries = res.data.map { it.query }
                    if (!queries.contains(searchQuery)) {
                        insertSearchHistoryQuery(searchQuery)
                    }
                }

                else -> {}
            }
        }
    }

    private fun insertSearchHistoryQuery(searchQuery: String) {
        launchCatching {
            roomStorageService.insertSearchHistoryQuery(
                SearchHistoryQueryEntity(
                    query = searchQuery,
                    timeStamp = getCurrentTimestamp()
                )
            )
        }
    }

    fun getData() {
        launchCatching {
            roomStorageService.getContactsWithNonEmptyLastMessageId().collect {
                _contacts.value = Response.Success(it)
            }
        }
        getCurrentUser()
        getSearchHistoryQueries()
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

    private fun getSearchHistoryQueries() {
        launchCatching {
            roomStorageService.getSearchHistoryQuery().collect {
                _searchQueries.value = Response.Success(it)
            }
        }
    }
}