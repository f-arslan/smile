package espressodev.smile.data.notification.model

data class PushNotification(
    val to: String = "",
    val data: NotificationData
)
