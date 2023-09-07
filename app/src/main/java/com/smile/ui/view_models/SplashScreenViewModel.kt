package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.model.datastore.DataStoreRepository
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.ui.screens.graph.SmileRoutes.ONBOARDING_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
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