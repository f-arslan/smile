package espressodev.smile.ui.view_models.login_screen_vm

import android.content.res.Resources
import espressodev.smile.SmileViewModel
import espressodev.smile.common.ext.isValidEmail
import espressodev.smile.common.snackbar.SnackbarManager
import espressodev.smile.model.service.AccountService
import espressodev.smile.model.service.LogService
import espressodev.smile.model.service.module.Response
import espressodev.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import espressodev.smile.R.string as AppText

@HiltViewModel
class ForgotPasswordScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val resources: Resources,
    logService: LogService
): SmileViewModel(logService) {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun sendPasswordResetEmail(clearAndNavigate: (String) -> Unit) {
        if (!_email.value.isValidEmail()) {
            SnackbarManager.showMessage(resources.getString(AppText.email_error))
            return
        }

        launchCatching {
            val response = accountService.sendPasswordResetEmail(_email.value.trim())
            if (response is Response.Success) {
                 clearAndNavigate(LOGIN_SCREEN)
            } else if (response is Response.Failure) {
                SnackbarManager.showMessage(response.e.message ?: EMAIL_ERROR)
            }
        }
    }
    companion object {
        private const val EMAIL_ERROR = "Email is not valid"
    }
}