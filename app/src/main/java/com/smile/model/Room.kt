package com.smile.model


data class Room(
    val roomId: String = "",
    val createdAt: Long = 0L,
    val lastMessage: Message = Message(),
)