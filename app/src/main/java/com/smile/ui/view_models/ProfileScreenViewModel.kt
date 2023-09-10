package com.smile.ui.view_models

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.smile.SmileViewModel
import com.smile.model.User
import com.smile.model.datastore.DataStoreRepository
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val dataStoreRepository: DataStoreRepository,
    logService: LogService,
) : SmileViewModel(logService) {

    private val _user = MutableStateFlow<Response<User>>(Response.Loading)
    val user = _user.asStateFlow()

    private val _notificationState = MutableStateFlow("")
    val notificationState = _notificationState.asStateFlow()

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
                    setNotificationsEnabled(DataStoreRepository.ENABLED)
                }
                else -> setNotificationsEnabled(DataStoreRepository.DISABLED)
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