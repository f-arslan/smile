package com.smile.ui.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.smile.SmileViewModel
import com.smile.common.ext.isValidEmail
import com.smile.common.ext.isValidPassword
import com.smile.common.ext.passwordMatches
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.User
import com.smile.model.google.domain.AuthRepository
import com.smile.model.google.domain.OneTapSignInResponse
import com.smile.model.google.domain.SignInWithGoogleResponse
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.smile.R.string as AppText
import com.smile.model.service.module.GoogleResponse.*
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val rePassword: String = "",
    val verificationState: Boolean = false,
    val loadingState: Boolean = false
)

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    private val authRepository: AuthRepository,
    val oneTapClient: SignInClient,
    logService: LogService
) : SmileViewModel(logService) {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse>(Success(null))
        private set

    private val _signInWithGoogleResponse = MutableStateFlow<SignInWithGoogleResponse>(Success(false))
    val signInWithGoogleResponse = _signInWithGoogleResponse.asStateFlow()


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
        launchCatching {
            onLoadingStateChange(true)
            val signUpResponse = accountService.firebaseSignUpWithEmailAndPassword(
                email = email,
                password = password
            )
            if (signUpResponse is Response.Success) {
                async { accountService.sendEmailVerification() }.await()
                saveToDatabase()
                onLoadingStateChange(false)
                delay(100)
                onVerificationStateChange(true)
            } else if (signUpResponse is Response.Failure) {
                onLoadingStateChange(false)
                signUpResponse.e.message?.let { SnackbarManager.showMessage(it) }
            }
        }
    }


    private fun saveToDatabase() {
        launchCatching {
            storageService.saveUser(
                User(
                    accountService.currentUserId,
                    uiState.value.name,
                    uiState.value.email
                )
            )
        }
    }

    fun oneTapSignUp() = launchCatching {
        oneTapSignInResponse = Loading
        oneTapSignInResponse = authRepository.oneTapSignUpWithGoogle()
    }

    fun signUpWithGoogle(googleCredential: AuthCredential) = launchCatching {
        oneTapSignInResponse = Loading
        _signInWithGoogleResponse.value = authRepository.firebaseSignInWithGoogle(googleCredential)
    }
}