package com.smile.model

data class User(
    val id: String,
    val isEmailVerified : Boolean = false,
)