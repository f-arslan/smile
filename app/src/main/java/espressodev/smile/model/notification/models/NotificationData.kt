package espressodev.smile.model.notification.models

data class NotificationData(
    val token: String = "",
    val title: String = "",
    val body: String = "",
    val roomId: String = "",
    val contactId: String = ""
)