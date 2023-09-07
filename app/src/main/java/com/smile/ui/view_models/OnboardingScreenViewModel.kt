package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.model.datastore.DataStoreRepository
import com.smile.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
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