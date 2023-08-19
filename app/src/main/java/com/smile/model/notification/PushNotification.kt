package com.smile.model.notification

data class PushNotification(
    val token: String = "",
    val title: String = "",
    val body: String = "",
    val roomId: String = "",
    val contactId: String = ""
)