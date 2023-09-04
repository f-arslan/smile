package com.smile.ui.view_models

import android.util.Log
import androidx.annotation.StringRes
import com.smile.SmileViewModel
import com.smile.common.ext.isValidPassword
import com.smile.common.ext.passwordMatches
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.smile.R.string as AppText

data class EditScreenUiState(
    val name: String = "",
    val password: String = "",
    val rePassword: String = "",
    @StringRes val topBarLabel: Int = AppText.edit_profile,
    val loadingState: Boolean = false
)

@HiltViewModel
class EditScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {

    private val _uiState = MutableStateFlow(EditScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val name
        get() = uiState.value.name

    private val password
        get() = uiState.value.password


    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(name = newName)
    }

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
        if (name.isBlank()) {
            SnackbarManager.showMessage(AppText.require_first_name)
            return
        }
        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }
        if (!password.passwordMatches(uiState.value.rePassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }
        updateUser(clearAndNavigate)
    }

    private fun updateUser(clearAndNavigate: (String) -> Unit) {
        onLoadingStateChange(true)
        launchCatching {
            val updatePasswordResponse = accountService.updatePassword(password)
            Log.d("EditScreenViewModel", "updateUser: $updatePasswordResponse")
            if (updatePasswordResponse is Response.Success) {
                Log.d("EditScreenViewModel", "updateUser2: $updatePasswordResponse")
                async { storageService.updateUserName(name) }.await()
                onLoadingStateChange(false)
                delay(50L)
                clearAndNavigate(HOME_SCREEN)
            }
        }
    }
}