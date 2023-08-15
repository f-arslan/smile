package com.smile.model.room

data class HomeContactEntity(
    val id: Int = 0,
    val contactId: String = "",
    val userId: String = "",
    val friendId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val roomId: String = "",
    val content: String = "",
    val timestamp: String = "",
)