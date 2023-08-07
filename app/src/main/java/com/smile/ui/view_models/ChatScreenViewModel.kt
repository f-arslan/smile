package com.smile.ui.view_models

import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.model.Contact
import com.smile.model.Message
import com.smile.model.MessageStatus
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import com.smile.util.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _contactState = MutableStateFlow<Response<Contact>>(Response.Loading)
    val contactState = _contactState.asStateFlow()

    private val _messagesState = MutableStateFlow<Response<List<Message>>>(Response.Loading)
    val messagesState = _messagesState.asStateFlow()

    fun sendMessage(text: String, recipientId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            storageService.sendMessage(
                Message(
                    senderId = accountService.currentUserId,
                    recipientId = recipientId,
                    content = text,
                    timestamp = getCurrentTimestamp(),
                    status = MessageStatus.SENT
                )
            )
        }
    }

    fun getMessage(contactId: String) {
        val recipientId = contactId.split("_")[1]
        viewModelScope.launch(Dispatchers.IO) {
            storageService.getMessages(recipientId) {
                _messagesState.value = Response.Success(it)
            }
        }
    }

    fun getContact(contactId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            storageService.getContact(contactId) {
                _contactState.value = Response.Success(it)
            }
        }
    }
}