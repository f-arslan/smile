package espressodev.smile.ui.screens.login.forgot_password

import android.content.res.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.common.ext.isValidEmail
import espressodev.smile.common.snackbar.SnackbarManager
import espressodev.smile.data.service.AccountService
import espressodev.smile.data.service.LogService
import espressodev.smile.data.service.model.Response
import espressodev.smile.ui.screens.login.LOGIN_GRAPH_ROUTE_PATTERN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import espressodev.smile.R.string as AppText

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val accountService: AccountService,
    private val resources: Resources,
    logService: LogService
) : SmileViewModel(logService) {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun sendPasswordResetEmail(clearAndNavigate: (String) -> Unit) {
        if (!email.value.isValidEmail()) {
            SnackbarManager.showMessage(resources.getString(AppText.email_error))
            return
        }

        launchCatching {
            val response = accountService.sendPasswordResetEmail(email.value.trim())
            if (response is Response.Success) {
                clearAndNavigate(LOGIN_GRAPH_ROUTE_PATTERN)
            } else if (response is Response.Failure) {
                SnackbarManager.showMessage(
                    response.e.message ?: resources.getString(AppText.email_error)
                )
            }
        }
    }
}