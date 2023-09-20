package espressodev.smile.data

import espressodev.smile.data.room.ContactEntity


data class Contact(
    val contactId: String = "",
    val userId: String = "",
    val friendId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val roomId: String = "",
) {
    fun toRoomContact(): ContactEntity {
        return ContactEntity(
            contactId = contactId,
            userId = userId,
            friendId = friendId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            roomId = roomId,
        )
    }
}