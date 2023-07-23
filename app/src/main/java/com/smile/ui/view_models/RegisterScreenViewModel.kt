package com.smile.ui.view_models

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.common.ext.isValidEmail
import com.smile.common.ext.isValidPassword
import com.smile.common.ext.passwordMatches
import com.smile.model.service.AuthService
import com.smile.model.service.SendEmailVerificationResponse
import com.smile.model.service.SignUpResponse
import com.smile.model.service.module.Response
import com.smile.util.Constants.EMAIL_ERROR
import com.smile.util.Constants.PASSWORD_ERROR
import com.smile.util.Constants.PASSWORD_MATCH_ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val rePassword: String = ""
)
@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val authService: AuthService
) : SmileViewModel() {
    private var _uiState by mutableStateOf(RegisterUiState())
    private var signUpResponse by mutableStateOf<SignUpResponse>(Response.Success(false))
    private var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(
        Response.Success(false)
    )
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

    fun onSignUpClick(snackbarHostState: SnackbarHostState, openAndPopUp: () -> Unit) {
        fun showSnackbar(message: String, duration: SnackbarDuration) {
            viewModelScope.launch(Dispatchers.IO) {
                snackbarHostState.showSnackbar(
                    message = message, duration = duration, withDismissAction = true
                )
            }
        }
        if (!email.isValidEmail()) {
            showSnackbar(EMAIL_ERROR, SnackbarDuration.Short)
            return
        }
        if (!password.isValidPassword()) {
            showSnackbar(PASSWORD_ERROR, SnackbarDuration.Short)
            return
        }
        if (!password.passwordMatches(uiState.rePassword)) {
            showSnackbar(PASSWORD_MATCH_ERROR, SnackbarDuration.Short)
            return
        }
        viewModelScope.launch {
            async { signUpWithEmailAndPassword(email, password) }.await()
            if (signUpResponse is Response.Failure) {
                showSnackbar(
                    (signUpResponse as Response.Failure).e.message ?: "", SnackbarDuration.Short
                )
                return@launch
            }
            async { sendEmailVerification() }.await()
            openAndPopUp()
        }
    }

    private suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        signUpResponse = Response.Loading
        signUpResponse = authService.firebaseSignUpWithEmailAndPassword(email, password)
    }

    private suspend fun sendEmailVerification() {
        sendEmailVerificationResponse = Response.Loading
        sendEmailVerificationResponse = authService.sendEmailVerification()
    }

    fun onGoogleClick() {

    }

}