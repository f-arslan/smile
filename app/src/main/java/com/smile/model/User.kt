package com.smile.model

import com.google.firebase.firestore.DocumentId
import com.smile.model.room.RoomUser

data class User(
    @DocumentId val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val isEmailVerified : Boolean = false,
    val contactIds: List<String> = emptyList(),
) {
    fun toRoomUser(): RoomUser {
        return RoomUser(
            userId = userId,
            displayName = displayName,
            email = email,
            profilePictureUrl = profilePictureUrl,
            isEmailVerified = isEmailVerified,
            contactIds = contactIds
        )
    }
}

data class Status(
    @DocumentId val statusId: String = "",
    val status: UserStatus = UserStatus.OFFLINE,
)

enum class UserStatus {
    OFFLINE, ONLINE
}
