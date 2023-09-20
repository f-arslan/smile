package espressodev.smile.ui.screens.profile_screen.notification_screen

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.data.datastore.DataStoreService
import espressodev.smile.data.datastore.DataStoreService.Companion.DISABLED
import espressodev.smile.data.datastore.DataStoreService.Companion.ENABLED
import espressodev.smile.data.service.LogService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    logService: LogService,
    private val dataStoreRepository: DataStoreService
) : SmileViewModel(logService) {

    private val _notificationPanelState = MutableStateFlow(false)
    val notificationPanelState = _notificationPanelState.asStateFlow()

    fun onNotificationPanelStateChange(newState: Boolean) {
        _notificationPanelState.value = newState
    }

    fun setNotificationsEnabled(notificationState: String, clearAndNavigate: () -> Unit = {}) {
        launchCatching {
            async { dataStoreRepository.setNotificationsEnabled(notificationState)}.await()
            _notificationPanelState.value = false
            if (notificationState in setOf(ENABLED, DISABLED)) {
                clearAndNavigate()
            }
        }
    }
}