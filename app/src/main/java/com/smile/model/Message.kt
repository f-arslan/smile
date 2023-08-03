package com.smile.model

import com.google.firebase.firestore.DocumentId

data class Message(
    @DocumentId val messageId: String = "",
    val senderId: String = "",
    val recipientId: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val readBy: List<String> = emptyList(), // user id's
    val status: MessageStatus = MessageStatus.NOT_SEND
)

enum class MessageStatus {
    NOT_SEND, SENT, DELIVERED, SEEN
}