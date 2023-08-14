package com.smile.ui.view_models

import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.model.room.ContactEntity
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    logService: LogService
): SmileViewModel(logService) {
    private val _contacts = MutableStateFlow<Response<List<List<ContactEntity>>>>(Response.Loading)
    val contacts = _contacts.asStateFlow()

    fun getContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            storageService.getContacts(viewModelScope) {
                _contacts.value = Response.Success(it)
            }
        }
    }
}

