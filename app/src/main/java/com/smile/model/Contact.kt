package com.smile.model

import com.google.firebase.firestore.DocumentId
import com.smile.model.room.RoomContact


data class Contact(
    @DocumentId
    val contactId: String = "",
    val userId: String = "",
    val friendId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val roomId: String = "",
) {
    fun toRoomContact(): RoomContact {
        return RoomContact(
            contactId = contactId,
            userId = userId,
            friendId = friendId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            roomId = roomId
        )
    }
}