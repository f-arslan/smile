package espressodev.smile.model.notification.models

import espressodev.smile.model.notification.NotificationService
import espressodev.smile.util.Secrets.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val notificationApi: NotificationService? by lazy {
        retrofit.create(NotificationService::class.java)
    }
}
