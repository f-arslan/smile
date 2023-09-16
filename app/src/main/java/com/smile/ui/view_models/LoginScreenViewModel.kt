package com.smile.ui.view_models

import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.smile.SmileViewModel
import com.smile.common.ext.isValidEmail
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.google.domain.AuthRepository
import com.smile.model.google.domain.OneTapSignInUpResponse
import com.smile.model.google.domain.SignInUpWithGoogleResponse
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.GoogleResponse.Loading
import com.smile.model.service.module.GoogleResponse.Success
import com.smile.model.service.module.Response
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.smile.R.string as AppText

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loadingState: Boolean = false,
    val oneTapSignInResponse: OneTapSignInUpResponse = Success(null),
    val signInWithGoogleResponse: SignInUpWithGoogleResponse = Success(false)
)

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    private val authRepository: AuthRepository,
    val oneTapClient: SignInClient,
    logService: LogService
) : SmileViewModel(logService) {


    private val _uiState = MutableStateFlow(LoginUiState("", ""))
    val uiState = _uiState.asStateFlow()


    fun onLoadingStateChange(loadingState: Boolean) {
        _uiState.value = _uiState.value.copy(loadingState = loadingState)
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue)
    }

    fun onLoginClick(openAndPopUp: (String) -> Unit) {
        if (!uiState.value.email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }
        if (uiState.value.password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }
        onLoadingStateChange(true)
        launchCatching {
            val signInResponse =
                accountService.firebaseSignInWithEmailAndPassword(
                    uiState.value.email,
                    uiState.value.password
                )
            if (signInResponse is Response.Success) {
                async { accountService.reloadFirebaseUser() }.await()
                if (accountService.isEmailVerified) {
                    onLoadingStateChange(false)
                    storageService.updateUserEmailVerification()
                    delay(100L)
                    openAndPopUp(HOME_SCREEN)
                } else {
                    SnackbarManager.showMessage(AppText.please_verify_email)
                }
            } else {
                SnackbarManager.showMessage(AppText.email_or_password_error)
            }
            onLoadingStateChange(false)
        }
    }


    fun checkEmailVerification() {
        viewModelScope.launch(Dispatchers.IO) {
            async { accountService.reloadFirebaseUser() }.await()
        }
    }

    fun oneTapSignIn() = launchCatching {
        _uiState.value = _uiState.value.copy(oneTapSignInResponse = Loading)
        _uiState.value = _uiState.value.copy(oneTapSignInResponse = authRepository.oneTapSignInWithGoogle())
    }

    fun signInWithGoogle(googleCredential: AuthCredential) = launchCatching {
        _uiState.value = _uiState.value.copy(signInWithGoogleResponse = Loading)
        _uiState.value = _uiState.value.copy(signInWithGoogleResponse = authRepository.firebaseSignInWithGoogle(googleCredential))
    }
}