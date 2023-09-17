package espressodev.smile.ui.view_models

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.model.datastore.DataStoreRepository
import espressodev.smile.model.service.LogService
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