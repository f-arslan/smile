package espressodev.smile.ui.view_models

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.model.datastore.DataStoreRepository
import espressodev.smile.model.service.AccountService
import espressodev.smile.model.service.LogService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {
    val isEmailVerified = accountService.isEmailVerified
    val onboardingScreenState = MutableStateFlow(false)

    fun getOnboardingScreenState() {
        launchCatching {
           onboardingScreenState.value =
                async { dataStoreRepository.getOnboardingScreenState() }.await()
        }
    }
}