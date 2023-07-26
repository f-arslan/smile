package com.smile.ui.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.common.ext.isValidEmail
import com.smile.common.ext.isValidPassword
import com.smile.common.ext.passwordMatches
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.SendEmailVerificationResponse
import com.smile.model.service.SignUpResponse
import com.smile.model.service.module.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.smile.R.string as AppText

data class RegisterUiState(
    val name: String = "fatih",
    val email: String = "fatiharslanedu@gmail.com",
    val password: String = "Mkal858858",
    val rePassword: String = "Mkal858858"
)

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {
    private var _uiState by mutableStateOf(RegisterUiState())

    var signUpResponse by mutableStateOf<SignUpResponse>(Response.Success(false))
        private set

    var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(
        Response.Success(false)
    )
        private set
    val uiState: RegisterUiState
        get() = _uiState

    private val email
        get() = uiState.email.trim()

    private val password
        get() = uiState.password

    fun onNameChange(newValue: String) {
        _uiState = _uiState.copy(name = newValue)
    }

    fun onEmailChange(newValue: String) {
        _uiState = _uiState.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState = _uiState.copy(password = newValue)
    }

    fun onRePasswordChange(newValue: String) {
        _uiState = _uiState.copy(rePassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: () -> Unit) {

        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }
        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }
        if (!password.passwordMatches(uiState.rePassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }
        viewModelScope.launch {
            async { signUpWithEmailAndPassword(email, password) }.await()
            if (signUpResponse is Response.Failure) {
                SnackbarManager.showMessage(AppText.generic_error)
                return@launch
            }
            async { sendEmailVerification() }.await()
            openAndPopUp()
        }
    }

    private suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        signUpResponse = Response.Loading
        signUpResponse = accountService.firebaseSignUpWithEmailAndPassword(email, password)
    }

    private suspend fun sendEmailVerification() {
        sendEmailVerificationResponse = Response.Loading
        sendEmailVerificationResponse = accountService.sendEmailVerification()
    }

    fun onGoogleClick() {

    }

}