package espressodev.smile.data.notification.models

data class PushNotification(
    val to: String = "",
    val data: NotificationData
)
