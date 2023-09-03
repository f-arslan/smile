package com.smile.ui.view_models

import android.util.Log
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

    fun getUserAndNotificationState() {
        launchCatching {
            storageService.user.collect {
                it?.let { _user.value = Response.Success(it) } ?: run {
                    Log.e(TAG, USER_IS_NULL)
                }
            }
        }
        getNotificationState()
    }

    private fun getNotificationState() {
        launchCatching {
            _notificationState.value = dataStoreRepository.getNotificationsEnabled()
        }
    }

    fun signOut(clearAndNavigate: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            clearAndNavigate(LOGIN_SCREEN)
        }
    }

    companion object {
        private const val TAG = "ProfileScreenViewModel"
        private const val USER_IS_NULL = "user is null"
    }
}