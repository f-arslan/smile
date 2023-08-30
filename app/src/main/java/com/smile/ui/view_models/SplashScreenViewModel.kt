package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {
    val isEmailVerified = accountService.isEmailVerified
}