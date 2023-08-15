package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.model.room.HomeContactEntity
import com.smile.model.room.RoomStorageService
import com.smile.model.service.LogService
import com.smile.model.service.module.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val roomStorageService: RoomStorageService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _contacts =
        MutableStateFlow<Response<List<HomeContactEntity>>>(Response.Loading)
    val contacts = _contacts.asStateFlow()

    fun getContacts() {
        launchCatching {
            roomStorageService.getContactsWithNonEmptyLastMessageId().collect {
                _contacts.value = Response.Success(it)
            }
        }
    }
}

