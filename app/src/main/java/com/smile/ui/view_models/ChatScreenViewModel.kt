package com.smile.ui.view_models

import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.model.Contact
import com.smile.model.Message
import com.smile.model.MessageStatus
import com.smile.model.room.ContactEntity
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import com.smile.util.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
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

    val currentUserId = accountService.currentUserId

    fun sendMessage(text: String, roomId: String, contactId: String) {
        if (_contactState.value is Response.Success) {
            val contact = (_contactState.value as Response.Success<Contact>).data
            viewModelScope.launch(Dispatchers.IO) {
                storageService.sendMessage(
                    viewModelScope,
                    Message(
                        senderId = accountService.currentUserId,
                        contactId = contactId,
                        contactName = contact.firstName + " " + contact.lastName,
                        content = text.trim(),
                        timestamp = getCurrentTimestamp(),
                        status = MessageStatus.SENT
                    ),
                    roomId,
                    contactId,
                )
            }
        }
    }

    fun getContactAndMessage(contactId: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val contactFlow = storageService.getContact(contactId)
            val messagesFlow = storageService.getMessages(viewModelScope, roomId, contactId)
            combine(contactFlow, messagesFlow) { contact, messages ->
                _contactState.value = Response.Success(contact)
                _messagesState.value = Response.Success(messages)
            }.collect()
        }
    }
}