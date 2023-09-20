package espressodev.smile.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import espressodev.smile.MainActivity
import espressodev.smile.data.service.StorageService
import espressodev.smile.util.Constants.CHANNEL_ID
import espressodev.smile.util.Constants.CHANNEL_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import espressodev.smile.R.drawable as AppDrawable

class FirebaseMessaging: FirebaseMessagingService() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface FirebaseMessagingEntryPoint {
        fun storageService(): StorageService
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FirebaseMessaging", "onMessageReceived: ${message.data}")
        sendNotification(message)
    }

    private fun sendNotification(message: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_MUTABLE
        )

        val channelId = CHANNEL_ID
        val title = message.data["title"]
        val body = message.data["body"]
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(AppDrawable.logo_black)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel =
            NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)

        manager.notify(1, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseMessaging", "onNewToken: $token")
        CoroutineScope(Dispatchers.IO).launch {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(applicationContext, FirebaseMessagingEntryPoint::class.java)
            hiltEntryPoint.storageService().saveFcmToken(token)
        }
    }
}