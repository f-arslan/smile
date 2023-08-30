package com.smile.ui.view_models

import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.common.ext.isValidEmail
import com.smile.common.ext.isValidPassword
import com.smile.common.ext.passwordMatches
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.User
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.smile.R.string as AppText

data class RegisterUiState(
    val name: String = "fatih",
    val email: String = "fatiharslanedu@gmail.com",
    val password: String = "Mkal858858",
    val rePassword: String = "Mkal858858",
    val verificationState: Boolean = false,
    val loadingState: Boolean = false
)

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val email
        get() = uiState.value.email

    private val password
        get() = uiState.value.password

    fun onLoadingStateChange(loadingState: Boolean) {
        _uiState.value = _uiState.value.copy(loadingState = loadingState)
    }

    fun onVerificationStateChange(verificationState: Boolean) {
        _uiState.value = _uiState.value.copy(verificationState = verificationState)
    }

    fun onNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(name = newValue)
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue)
    }

    fun onRePasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(rePassword = newValue)
    }

    fun onSignUpClick() {
        onLoadingStateChange(true)
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
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
        if (uiState.value.name.isBlank()) {
            SnackbarManager.showMessage(AppText.require_first_name)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val signUpResponse = accountService.firebaseSignUpWithEmailAndPassword(
                email = email,
                password = password
            )
            if (signUpResponse is Response.Success)
            {
                async { accountService.sendEmailVerification() }.await()
                saveToDatabase()
                onLoadingStateChange(false)
                delay(100)
                onVerificationStateChange(true)
            }
        }
    }


    private fun saveToDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            storageService.saveUser(
                User(
                    accountService.currentUserId,
                    uiState.value.name,
                    uiState.value.email
                )
            )
        }
    }

    fun onGoogleClick() {

    }
}