package espressodev.smile.model


data class Room(
    val roomId: String = "",
    val createdAt: Long = 0L,
    val contacts: List<Contact> = emptyList(),
    val lastMessage: Message = Message(),
)