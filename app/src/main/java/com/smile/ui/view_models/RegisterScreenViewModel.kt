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
import com.smile.model.service.SignUpResponse
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
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
    private val storageService: StorageService,
    logService: LogService
) : SmileViewModel(logService) {
    private var _uiState by mutableStateOf(RegisterUiState())

    private var signUpResponse by mutableStateOf<SignUpResponse>(Response.Success(false))
    private var sendEmailVerificationResponse by mutableStateOf<Response<Boolean>>(
        Response.Success(
            false
        )
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
            async {
                signUpResponse = accountService.firebaseSignUpWithEmailAndPassword(email, password)
            }.await()
            if (signUpResponse is Response.Failure) {
                SnackbarManager.showMessage((signUpResponse as Response.Failure).e.message.toString())
                return@launch
            }
            if (signUpResponse is Response.Success && !(signUpResponse as Response.Success<Boolean>).data)
                return@launch
            async { sendEmailVerificationResponse = accountService.sendEmailVerification() }.await()
            if (sendEmailVerificationResponse is Response.Failure) {
                SnackbarManager.showMessage((sendEmailVerificationResponse as Response.Failure).e.message.toString())
                return@launch
            }
            openAndPopUp()
        }
    }


    fun onGoogleClick() {

    }
}