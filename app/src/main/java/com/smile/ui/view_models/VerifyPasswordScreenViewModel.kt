package com.smile.ui.view_models

import com.smile.R
import com.smile.SmileViewModel
import com.smile.common.ext.isValidPassword
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.module.Response
import com.smile.ui.screens.graph.SmileRoutes.CHANGE_PASSWORD_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class VerifyPasswordScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
): SmileViewModel(logService) {
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onLoadingStateChange(loadingState: Boolean) {
        _loadingState.value = loadingState
    }

    fun onConfirmClick(clearAndNavigate: (String) -> Unit) {
        if (!_password.value.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_error)
            return
        }
        onLoadingStateChange(true)
        launchCatching {
            val signInResponse = accountService.firebaseSignInWithEmailAndPassword(accountService.email, _password.value)
            if (signInResponse is Response.Success) {
                clearAndNavigate(CHANGE_PASSWORD_SCREEN)
            } else {
                SnackbarManager.showMessage(R.string.password_match_error)
            }
            onLoadingStateChange(false)
            delay(50L)
        }
    }
}