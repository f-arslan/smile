package com.smile.model

import com.google.firebase.firestore.DocumentId
import com.smile.model.room.MessageEntity

data class Message(
    @DocumentId val messageId: String = "",
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val readBy: List<String> = emptyList(), // user id's
    val status: MessageStatus = MessageStatus.NOT_SEND
) {
    fun toMessageEntity() = MessageEntity(
        messageId = messageId,
        senderId = senderId,
        content = content,
        timestamp = timestamp
    )
}

enum class MessageStatus {
    NOT_SEND, SENT, SEEN
}