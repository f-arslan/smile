package com.smile.model

import com.google.firebase.firestore.DocumentId
import com.smile.model.room.ContactEntity


data class Contact(
    @DocumentId
    val contactId: String = "",
    val userId: String = "",
    val friendId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val roomId: String = "",
    val lastMessageId: String = ""
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
            lastMessageId = lastMessageId
        )
    }
}