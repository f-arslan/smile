package espressodev.smile.ui.screens.chat

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.data.service.AccountService
import espressodev.smile.data.service.LogService
import espressodev.smile.data.service.StorageService
import espressodev.smile.data.service.model.Contact
import espressodev.smile.data.service.model.Message
import espressodev.smile.data.service.model.MessageStatus
import espressodev.smile.data.service.model.Response
import espressodev.smile.domain.usecase.GetContactAndMessagesUseCase
import espressodev.smile.domain.util.getCurrentTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    accountService: AccountService,
    private val storageService: StorageService,
    private val getContactAndMessagesUseCase: GetContactAndMessagesUseCase,
    logService: LogService
) : SmileViewModel(logService) {
    private val _contactState = MutableStateFlow<Response<Contact>>(Response.Loading)
    val contactState = _contactState.asStateFlow()

    private val _messagesState = MutableStateFlow<Response<List<Message>>>(Response.Loading)
    val messagesState = _messagesState.asStateFlow()

    val currentUserId = accountService.currentUserId
    fun sendMessage(text: String, roomId: String, contactId: String) = launchCatching {
        if (contactState.value is Response.Success) {
            val contact = (contactState.value as Response.Success<Contact>).data
            val message = Message(
                senderId = currentUserId,
                contactId = contactId,
                contactName = contact.toFullName(),
                content = text.trim(),
                timestamp = getCurrentTimestamp(),
                status = MessageStatus.SENT
            )
            storageService.sendMessage(message, roomId, contactId)
        }
    }

    fun getContactAndMessage(contactId: String, roomId: String) = launchCatching {
        getContactAndMessagesUseCase.invoke(contactId, roomId).collect {
            val (contact, messages) = it
            _contactState.value = contact
            _messagesState.value = messages
        }
    }
}