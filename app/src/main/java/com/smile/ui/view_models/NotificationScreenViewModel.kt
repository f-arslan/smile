package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.model.datastore.DataStoreRepository
import com.smile.model.datastore.DataStoreRepository.Companion.DISABLED
import com.smile.model.datastore.DataStoreRepository.Companion.ENABLED
import com.smile.model.datastore.DataStoreRepository.Companion.IDLE
import com.smile.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    logService: LogService,
    private val dataStoreRepository: DataStoreRepository
) : SmileViewModel(logService) {

    private val _notificationPanelState = MutableStateFlow(false)
    val notificationPanelState = _notificationPanelState.asStateFlow()

    fun onNotificationPanelStateChange(newState: Boolean) {
        _notificationPanelState.value = newState
    }

    fun setNotificationsEnabled(notificationState: String, clearAndNavigate: () -> Unit) {
        launchCatching {
            async { dataStoreRepository.setNotificationsEnabled(notificationState)}.await()
            _notificationPanelState.value = false
            if (notificationState == ENABLED) {
                clearAndNavigate()
            }
        }
    }
}