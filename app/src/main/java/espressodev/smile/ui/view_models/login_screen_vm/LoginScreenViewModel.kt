package espressodev.smile.ui.view_models.login_screen_vm

import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import espressodev.smile.SmileViewModel
import espressodev.smile.common.ext.isValidEmail
import espressodev.smile.common.snackbar.SnackbarManager
import espressodev.smile.model.google.domain.AuthRepository
import espressodev.smile.model.google.domain.OneTapSignInUpResponse
import espressodev.smile.model.google.domain.SignInUpWithGoogleResponse
import espressodev.smile.model.service.AccountService
import espressodev.smile.model.service.LogService
import espressodev.smile.model.service.StorageService
import espressodev.smile.model.service.module.GoogleResponse.Loading
import espressodev.smile.model.service.module.GoogleResponse.Success
import espressodev.smile.model.service.module.LoadingState
import espressodev.smile.model.service.module.Response
import espressodev.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import espressodev.smile.R.string as AppText

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loadingState: LoadingState = LoadingState.Idle,
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
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newValue: String) {
        updateUiState { copy(email = newValue) }
    }

    fun onLoadingStateChange(loadingState: LoadingState) {
        updateUiState { copy(loadingState = loadingState) }
    }

    fun onPasswordChange(newValue: String) {
        updateUiState { copy(password = newValue) }
    }

    fun onLoginClick(openAndPopUp: (String) -> Unit) {
        onLoadingStateChange(LoadingState.Loading)
        if (!uiState.value.email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }
        if (uiState.value.password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }
        launchCatching {
            val signInResponse =
                accountService.firebaseSignInWithEmailAndPassword(
                    uiState.value.email,
                    uiState.value.password
                )
            if (signInResponse is Response.Success) {
                val reloadResponse = accountService.reloadFirebaseUser()
                if (reloadResponse is Response.Success && accountService.isEmailVerified) {
                    storageService.updateUserEmailVerification()
                    openAndPopUp(HOME_SCREEN)
                } else {
                    SnackbarManager.showMessage(AppText.please_verify_email)
                }
            } else {
                SnackbarManager.showMessage(AppText.email_or_password_error)
            }
            onLoadingStateChange(LoadingState.Idle)
        }
    }


    fun checkEmailVerification() {
        viewModelScope.launch(Dispatchers.IO) {
            async { accountService.reloadFirebaseUser() }.await()
        }
    }

    fun oneTapSignIn() = launchCatching {
        updateUiState { copy(oneTapSignInResponse = Loading) }
        updateUiState { copy(oneTapSignInResponse = authRepository.oneTapSignInWithGoogle()) }
    }

    fun signInWithGoogle(googleCredential: AuthCredential) = launchCatching {
        updateUiState { copy(signInWithGoogleResponse = Loading) }
        updateUiState {
            copy(
                signInWithGoogleResponse = authRepository.firebaseSignInWithGoogle(
                    googleCredential
                )
            )
        }
    }

    private inline fun updateUiState(block: LoginUiState.() -> LoginUiState) {
        _uiState.value = _uiState.value.block()
    }
}