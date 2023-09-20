package espressodev.smile.ui.view_models

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.data.datastore.DataStoreRepository
import espressodev.smile.data.service.LogService
import javax.inject.Inject

@HiltViewModel
class OnboardingScreenViewModel  @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    logService: LogService
): SmileViewModel(logService){
    fun setOnboardingScreenState() {
        launchCatching {
            dataStoreRepository.setOnboardingScreenState(true)
        }
    }
}