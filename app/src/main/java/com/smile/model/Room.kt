package com.smile.model

import com.google.firebase.firestore.DocumentId

data class Room(
    @DocumentId val roomId: String = "",
    val senderId: String = "",
    val participants: List<String> = emptyList(), // user id's
    val createdAt: Long = 0L
)