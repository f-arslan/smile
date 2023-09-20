package espressodev.smile.ui.view_models.profile_screen_vm

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.data.datastore.DataStoreRepository
import espressodev.smile.data.datastore.DataStoreRepository.Companion.DISABLED
import espressodev.smile.data.datastore.DataStoreRepository.Companion.ENABLED
import espressodev.smile.data.service.LogService
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