package com.smile.model.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smile.model.service.StorageService
import com.smile.util.Secrets.CHANNEL_ID
import com.smile.util.Secrets.CHANNEL_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.smile.R.drawable as AppDrawable

class FirebaseMessaging : FirebaseMessagingService() {

    @Inject
    lateinit var storageService: StorageService
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FirebaseMessaging", "onMessageReceived: ${message.data}")
        sendNotification(message)
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(message: RemoteMessage) {
        message.notification?.let {
            val title = it.title
            val body = it.body
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
            val notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(AppDrawable.logo_black)
                .setAutoCancel(true)
                .build()
            NotificationManagerCompat.from(this).notify(1, notification)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            storageService.saveFcmToken(token)
        }
    }
}