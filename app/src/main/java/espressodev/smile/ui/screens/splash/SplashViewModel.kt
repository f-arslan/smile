package espressodev.smile.ui.screens.splash

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.data.datastore.DataStoreService
import espressodev.smile.data.service.AccountService
import espressodev.smile.data.service.LogService
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreService,
    accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {
    val isEmailVerified = accountService.isEmailVerified
    val onboardingScreenState = MutableStateFlow(false)

    fun getOnboardingScreenState() = launchCatching {
        onboardingScreenState.value =
            dataStoreRepository.getOnboardingScreenState()
    }
}