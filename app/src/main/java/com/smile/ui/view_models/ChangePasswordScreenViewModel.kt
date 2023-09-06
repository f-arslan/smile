package com.smile.ui.view_models

import android.util.Log
import androidx.annotation.StringRes
import com.smile.SmileViewModel
import com.smile.common.ext.isValidPassword
import com.smile.common.ext.passwordMatches
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.module.Response
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.smile.R.string as AppText

data class ChangePasswordScreenUiState(
    val password: String = "",
    val rePassword: String = "",
    @StringRes val topBarLabel: Int = AppText.change_password,
    val loadingState: Boolean = false
)

@HiltViewModel
class ChangePasswordScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {

    private val _uiState = MutableStateFlow(ChangePasswordScreenUiState())
    val uiState = _uiState.asStateFlow()


    private val password
        get() = uiState.value.password


    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onRePasswordChange(newRePassword: String) {
        _uiState.value = _uiState.value.copy(rePassword = newRePassword)
    }

    fun onLoadingStateChange(loadingState: Boolean) {
        _uiState.value = _uiState.value.copy(loadingState = loadingState)
    }

    fun onUpdateClick(clearAndNavigate: (String) -> Unit) {
        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }
        if (!password.passwordMatches(uiState.value.rePassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }
        changePassword(clearAndNavigate)
    }

    private fun changePassword(clearAndNavigate: (String) -> Unit) {
        onLoadingStateChange(true)
        launchCatching {
            val updatePasswordResponse = accountService.updatePassword(password)
            Log.d("EditScreenViewModel", "updateUser: $updatePasswordResponse")
            if (updatePasswordResponse is Response.Success) {
                onLoadingStateChange(false)
                delay(100L)
                clearAndNavigate(LOGIN_SCREEN)
            } else if (updatePasswordResponse is Response.Failure) {
                SnackbarManager.showMessage(updatePasswordResponse.e.message.orEmpty())
                onLoadingStateChange(false)
                delay(100L)
            }
        }
    }
}