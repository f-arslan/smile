package espressodev.smile.data.notification.module

import espressodev.smile.data.notification.NotificationService
import espressodev.smile.domain.util.Constants.BASE_URL
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
