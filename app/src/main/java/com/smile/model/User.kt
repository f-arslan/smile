package com.smile.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val isEmailVerified : Boolean = false,
    val contactIds: List<String> = emptyList(),
    val roomIds: List<String> = emptyList(),
    val fcmToken: String = "",
)

data class Status(
    @DocumentId val statusId: String = "",
    val status: UserStatus = UserStatus.OFFLINE,
)

enum class UserStatus {
    OFFLINE, ONLINE
}
