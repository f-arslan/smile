package com.smile.model

data class Message(
    val id: String,
    val senderId: String,
    val recipientId: String,
    val text: String,
    val timestamp: Long,
    val status: MessageStatus
)

enum class MessageStatus {
    SENT, DELIVERED, SEEN
}