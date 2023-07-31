package com.smile.model

import com.google.firebase.firestore.DocumentId

data class Message(
    @DocumentId val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val status: MessageStatus = MessageStatus.NOT_SEND
)

enum class MessageStatus {
    NOT_SEND, SENT, DELIVERED, SEEN
}