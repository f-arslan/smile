package espressodev.smile.ui.screens.onboarding_screen

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.data.datastore.DataStoreService
import espressodev.smile.data.service.LogService
import javax.inject.Inject

@HiltViewModel
class OnboardingScreenViewModel  @Inject constructor(
    private val dataStoreRepository: DataStoreService,
    logService: LogService
): SmileViewModel(logService){
    fun setOnboardingScreenState() {
        launchCatching {
            dataStoreRepository.setOnboardingScreenState(true)
        }
    }
}