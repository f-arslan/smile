package com.smile.model

import com.google.firebase.firestore.DocumentId

data class Room(
    @DocumentId val roomId: String = "",
    val createdAt: Long = 0L,
    val lastMessage: Message = Message()
)