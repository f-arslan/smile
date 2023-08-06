package com.smile.ui.view_models

import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.model.Contact
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
class ChatScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _contactState = MutableStateFlow<Response<Contact>>(Response.Loading)
    val contactState = _contactState.asStateFlow()

    fun addMessage(text: String) {

    }

    fun getContact(contactId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            storageService.getContact(contactId) {
                _contactState.value = Response.Success(it)
            }
        }
    }
}