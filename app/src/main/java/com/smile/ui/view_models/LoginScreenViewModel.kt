package com.smile.ui.view_models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.common.ext.isValidEmail
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.SignInResponse
import com.smile.model.service.module.Response
import com.smile.model.service.module.map
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.smile.R.string as AppText

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {
    private var _uiState by mutableStateOf(LoginUiState("fatiharslanedu@gmail.com", "Mkal858858"))
    val uiState: LoginUiState
        get() = _uiState

    private val signInResponse = mutableStateOf<SignInResponse>(Response.Success(false))
    private var emailVerification = mutableStateOf(false)

    fun onEmailChange(newValue: String) {
        _uiState = _uiState.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState = _uiState.copy(password = newValue)
    }

    fun onLoginClick(openAndPopUp: (String) -> Unit) {
        if (!uiState.email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }
        if (uiState.password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        launchCatching {
            signInResponse.value =
                accountService.firebaseSignInWithEmailAndPassword(uiState.email, uiState.password)
            signInResponse.value.map { loggedIn ->
                if (!loggedIn) {
                    SnackbarManager.showMessage(AppText.email_or_password_error)
                    return@map
                }
                if (emailVerification.value) {
                    openAndPopUp(HOME_SCREEN)
                } else {
                    SnackbarManager.showMessage(AppText.please_verify_email)
                    return@map
                }
            }
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch(Dispatchers.IO) {
            accountService.reloadFirebaseUser()
            if (accountService.isEmailVerified)
                emailVerification.value = true
        }
    }

    fun onGoogleLoginClick() {

    }

}