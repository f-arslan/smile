package com.smile.model

import com.google.firebase.firestore.DocumentId
import com.smile.model.room.RoomEntity

data class Room(
    @DocumentId val roomId: String = "",
    val createdAt: Long = 0L,
    val lastMessage: Message = Message()
) {
    fun toRoomEntity() = RoomEntity(
        roomId = roomId,
        createdAt = createdAt,
    )
}