package com.smile.model.notification.models

data class PushNotification(
    val to: String = "",
    val data: NotificationData
)
