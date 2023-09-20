package espressodev.smile.data.notification

import espressodev.smile.data.notification.models.PushNotification
import espressodev.smile.util.Constants.CONTENT_TYPE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import espressodev.smile.BuildConfig.FCM_SERVER_KEY


interface NotificationService {
    @Headers("Authorization: key=$FCM_SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>
}
