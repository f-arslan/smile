package com.smile.ui.view_models

import androidx.compose.animation.core.updateTransition
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.smile.SmileViewModel
import com.smile.common.ext.isValidEmail
import com.smile.common.ext.isValidPassword
import com.smile.common.ext.passwordMatches
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.User
import com.smile.model.google.domain.AuthRepository
import com.smile.model.google.domain.OneTapSignInUpResponse
import com.smile.model.google.domain.SignInUpWithGoogleResponse
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.GoogleResponse.Loading
import com.smile.model.service.module.GoogleResponse.Success
import com.smile.model.service.module.LoadingState
import com.smile.model.service.module.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.smile.R.string as AppText

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val rePassword: String = "",
    val verificationState: Boolean = false,
    val loadingState: LoadingState = LoadingState.Idle,
    val oneTapSignUpResponse: OneTapSignInUpResponse = Success(null),
    val signUpWithGoogleResponse: SignInUpWithGoogleResponse = Success(false)
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

    private val email
        get() = uiState.value.email

    private val password
        get() = uiState.value.password

    fun onLoadingStateChange(loadingState: LoadingState) {
        updateUiState { copy(loadingState = loadingState) }
    }

    fun onVerificationStateChange(verificationState: Boolean) {
        updateUiState { copy(verificationState = verificationState) }
    }

    fun onNameChange(newValue: String) {
        updateUiState { copy(name = newValue) }
    }

    fun onEmailChange(newValue: String) {
        updateUiState { copy(email = newValue) }
    }

    fun onPasswordChange(newValue: String) {
        updateUiState { copy(password = newValue) }
    }

    fun onRePasswordChange(newValue: String) {
        updateUiState { copy(rePassword = newValue) }
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
        onLoadingStateChange(LoadingState.Loading)
        launchCatching {
            val signUpResponse = accountService.firebaseSignUpWithEmailAndPassword(
                email = email,
                password = password
            )
            if (signUpResponse is Response.Success) {
                async { accountService.sendEmailVerification() }.await()
                saveToDatabase()
                onVerificationStateChange(true)
            } else if (signUpResponse is Response.Failure) {
                signUpResponse.e.message?.let { SnackbarManager.showMessage(it) }
            }
            onLoadingStateChange(LoadingState.Idle)
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
        updateUiState { copy(oneTapSignUpResponse = Loading) }
        updateUiState { copy(oneTapSignUpResponse = authRepository.oneTapSignUpWithGoogle()) }
    }

    fun signUpWithGoogle(googleCredential: AuthCredential) = launchCatching {
        updateUiState { copy(signUpWithGoogleResponse = Loading) }
        updateUiState { copy(signUpWithGoogleResponse = authRepository.firebaseSignInWithGoogle(googleCredential)) }
    }

    private inline fun updateUiState(updateBlock: RegisterUiState.() -> RegisterUiState) {
        _uiState.value = _uiState.value.updateBlock()
    }
}