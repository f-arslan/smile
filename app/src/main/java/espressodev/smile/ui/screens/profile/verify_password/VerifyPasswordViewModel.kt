package espressodev.smile.ui.screens.profile.verify_password

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.R
import espressodev.smile.SmileViewModel
import espressodev.smile.common.ext.isValidPassword
import espressodev.smile.common.snackbar.SnackbarManager
import espressodev.smile.data.service.AccountService
import espressodev.smile.data.service.LogService
import espressodev.smile.data.service.model.Response
import espressodev.smile.ui.screens.profile.change_password.changePasswordRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class VerifyPasswordViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onLoadingStateChange(loadingState: Boolean) {
        _loadingState.value = loadingState
    }

    fun onConfirmClick(clearAndNavigate: (String) -> Unit) {
        if (!_password.value.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_error)
            return
        }
        onLoadingStateChange(true)
        launchCatching {
            val signInResponse = accountService.firebaseSignInWithEmailAndPassword(
                accountService.email,
                _password.value
            )
            if (signInResponse is Response.Success) {
                onLoadingStateChange(false)
                delay(100L)
                clearAndNavigate(changePasswordRoute)
            } else if (signInResponse is Response.Failure) {
                onLoadingStateChange(false)
                delay(100L)
                SnackbarManager.showMessage(R.string.password_match_error)
            }
        }
    }
}