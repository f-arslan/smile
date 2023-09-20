package espressodev.smile.domain.usecase

import espressodev.smile.data.service.StorageService
import espressodev.smile.data.service.model.Contact
import espressodev.smile.data.service.model.Message
import espressodev.smile.data.service.model.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetContactAndMessagesUseCase @Inject constructor(private val storageService: StorageService) {
    suspend operator fun invoke(
        contactId: String,
        roomId: String
    ): Flow<Pair<Response<Contact>, Response<List<Message>>>> {
        return combine(
            storageService.getContact(contactId),
            storageService.getMessages(roomId, contactId)
        ) { contact, messages ->
            Pair(Response.Success(contact), Response.Success(messages))
        }.flowOn(Dispatchers.IO)
    }
}
