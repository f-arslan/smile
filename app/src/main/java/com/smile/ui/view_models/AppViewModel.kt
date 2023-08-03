package com.smile.ui.view_models

import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.module.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppViewModel @Inject constructor(
    private val authService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {

}