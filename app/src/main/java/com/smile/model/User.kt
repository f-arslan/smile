package com.smile.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val userId: String = "",
    val displayName: String = "",
    val profilePictureUrl: String = "",
    val friends: List<String> = emptyList(), // user id's
    val isEmailVerified : Boolean = false,
    val status: UserStatus = UserStatus.OFFLINE
)

enum class UserStatus {
    OFFLINE, ONLINE
}
