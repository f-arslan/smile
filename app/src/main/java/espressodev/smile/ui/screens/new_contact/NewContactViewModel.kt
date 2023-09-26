package espressodev.smile.ui.screens.new_contact

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.common.ext.isValidEmail
import espressodev.smile.common.snackbar.SnackbarManager
import espressodev.smile.data.service.AccountService
import espressodev.smile.data.service.LogService
import espressodev.smile.data.service.StorageService
import espressodev.smile.data.service.model.Contact
import espressodev.smile.data.service.model.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import espressodev.smile.R.string as AppText

data class NewContactUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
)

@HiltViewModel
class NewContactScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _uiState = MutableStateFlow(NewContactUiState())
    val uiState = _uiState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    fun onFirstNameChange(firstName: String) {
        _uiState.value = _uiState.value.copy(firstName = firstName.trim())
    }

    fun onLastNameChange(lastName: String) {
        _uiState.value = _uiState.value.copy(lastName = lastName.trim())
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email.trim())
    }

    fun onLoadingStateChange(loadingState: Boolean) {
        _loadingState.value = loadingState
    }

    fun onSaveClick(popUp: () -> Unit) {
        uiState.value.apply {
            if (firstName.isBlank()) {
                SnackbarManager.showMessage(AppText.require_first_name)
                return
            }
            if (lastName.isBlank()) {
                SnackbarManager.showMessage(AppText.require_last_name)
                return
            }
            if (!email.isValidEmail()) {
                SnackbarManager.showMessage(AppText.email_error)
                return
            }
        }
        saveContactToDb(popUp)
    }

    private fun saveContactToDb(popUp: () -> Unit) {
        onLoadingStateChange(true)
        launchCatching {
            val contactUserId = storageService.findIdByEmail(uiState.value.email)
            if (contactUserId is Response.Failure) {
                SnackbarManager.showMessage(AppText.user_not_found)
                onLoadingStateChange(false)
                return@launchCatching
            }
            storageService.user.collect {
                if (it != null && contactUserId is Response.Success) {
                    val firstContact = Contact(
                        userId = accountService.currentUserId,
                        friendId = contactUserId.data,
                        firstName = uiState.value.firstName,
                        lastName = uiState.value.lastName,
                        email = uiState.value.email
                    )
                    val secondContact = Contact(
                        userId = contactUserId.data,
                        friendId = accountService.currentUserId,
                        firstName = it.displayName,
                        email = it.email
                    )
                    storageService.saveContact(viewModelScope, firstContact, secondContact)
                    onLoadingStateChange(false)
                    delay(100L)
                    popUp()
                }
            }
        }
    }
}
