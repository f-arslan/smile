package com.smile.model

data class User(
    val id: String = "",
    val displayName: String = "",
    val friends: List<String> = emptyList(),
    val profilePictureUrl: String = "",
    val isEmailVerified : Boolean = false,
    val status: UserStatus = UserStatus.OFFLINE
)

enum class UserStatus {
    OFFLINE, ONLINE
}
