package com.smile.ui.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.smile.SmileViewModel
import com.smile.common.ext.isValidEmail
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.SignInResponse
import com.smile.model.service.module.Response
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private var signInResponse by mutableStateOf<SignInResponse>(Response.Success(false))

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
            signInResponse =
                accountService.firebaseSignInWithEmailAndPassword(uiState.email, uiState.password)
            when (val result = signInResponse) {
                is Response.Success -> {
                    if (result.data) {
                        openAndPopUp(HOME_SCREEN)

                    } else {
                        SnackbarManager.showMessage(AppText.email_or_password_error)
                    }
                }
                else -> {
                    SnackbarManager.showMessage(AppText.email_or_password_error)
                }
            }
        }
    }

    fun onGoogleLoginClick() {

    }

}