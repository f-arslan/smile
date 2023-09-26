package espressodev.smile.ui.screens.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import espressodev.smile.SmileViewModel
import espressodev.smile.data.service.model.User
import espressodev.smile.data.datastore.DataStoreService
import espressodev.smile.data.google.GoogleProfileService
import espressodev.smile.data.service.AccountService
import espressodev.smile.data.service.LogService
import espressodev.smile.data.service.StorageService
import espressodev.smile.data.service.model.GoogleResponse.*
import espressodev.smile.data.service.model.Response
import espressodev.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.data.google.SignOutResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject



@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val dataStoreRepository: DataStoreService,
    private val profileRepository: GoogleProfileService,
    logService: LogService,
) : SmileViewModel(logService) {

    private val _user = MutableStateFlow<Response<User>>(Response.Loading)
    val user = _user.asStateFlow()

    private val _notificationState = MutableStateFlow("")
    val notificationState = _notificationState.asStateFlow()

    val displayName get() = profileRepository.displayName
    val photoUrl get() = profileRepository.photoUrl

    var signOutResponse by mutableStateOf<SignOutResponse>(Success(false))
        private set

    fun getUserAndNotificationState(context: Context) {
        launchCatching {
            storageService.user.collect {
                it?.let { _user.value = Response.Success(it) } ?: run {
                    Log.e(TAG, USER_IS_NULL)
                }
            }
        }
        getNotificationState(context)
    }

    private fun setNotificationsEnabled(notificationState: String) {
        launchCatching {
            dataStoreRepository.setNotificationsEnabled(notificationState)
        }
    }

    private fun getNotificationState(context: Context) {
        launchCatching {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setNotificationsEnabled(DataStoreService.ENABLED)
                }
                else -> setNotificationsEnabled(DataStoreService.DISABLED)
            }
            _notificationState.value = dataStoreRepository.getNotificationsEnabled()
        }
    }

    fun signOut(clearAndNavigate: (String) -> Unit) {
        launchCatching {
            Firebase.messaging.deleteToken().await()
            dataStoreRepository.setFcmToken("")
            accountService.signOut()
            clearAndNavigate(LOGIN_SCREEN)
        }
    }

    companion object {
        private const val TAG = "ProfileScreenViewModel"
        private const val USER_IS_NULL = "user is null"
    }
}