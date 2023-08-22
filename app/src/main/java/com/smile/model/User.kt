package com.smile.model

import com.google.firebase.firestore.DocumentId
import com.smile.model.room.UserEntity

data class User(
    @DocumentId val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val isEmailVerified : Boolean = false,
    val contactIds: List<String> = emptyList(),
    val fcmToken: String = "",
) {
    fun toRoomUser(): UserEntity {
        return UserEntity(
            userId = userId,
            displayName = displayName,
            email = email,
            profilePictureUrl = profilePictureUrl,
            isEmailVerified = isEmailVerified,
            fcmToken = fcmToken
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
