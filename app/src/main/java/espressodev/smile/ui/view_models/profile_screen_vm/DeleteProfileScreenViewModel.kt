package espressodev.smile.ui.view_models.profile_screen_vm

import android.content.res.Resources
import espressodev.smile.SmileViewModel
import espressodev.smile.common.snackbar.SnackbarManager
import espressodev.smile.model.datastore.DataStoreRepository
import espressodev.smile.model.service.AccountService
import espressodev.smile.model.service.LogService
import espressodev.smile.model.service.StorageService
import espressodev.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import espressodev.smile.R.string as AppText

@HiltViewModel
class DeleteProfileScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val dataStoreRepository: DataStoreRepository,
    private val resources: Resources,
    logService: LogService
): SmileViewModel(logService) {

    private val _alertDialogState = MutableStateFlow(false)
    val alertDialogState = _alertDialogState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    fun onAlertDialogStateChange(state: Boolean) {
        _alertDialogState.value = state
    }

    fun onLoadingStateChange(state: Boolean) {
        _loadingState.value = state
    }


    fun onDeleteClick(clearAndNavigate: (String) -> Unit) {
        launchCatching {
            onAlertDialogStateChange(false)
            delay(50L)
            onLoadingStateChange(true)
            val user = storageService.getUser()
            if (user == null) {
                SnackbarManager.showMessage(resources.getString(AppText.user_not_found))
                return@launchCatching
            }
            storageService.deleteUser()
            dataStoreRepository.clearAllInformation()
            accountService.revokeAccess()
            onLoadingStateChange(false)
            delay(100L)
            clearAndNavigate(LOGIN_SCREEN)
        }
    }
}