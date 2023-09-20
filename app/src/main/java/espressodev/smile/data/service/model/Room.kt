package espressodev.smile.data.service.model

import espressodev.smile.data.service.model.Contact
import espressodev.smile.data.service.model.Message


data class Room(
    val roomId: String = "",
    val createdAt: Long = 0L,
    val contacts: List<Contact> = emptyList(),
    val lastMessage: Message = Message(),
)