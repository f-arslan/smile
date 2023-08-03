package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.common.ext.isValidName
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.smile.R.string as AppText

data class NewContactUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
)

@HiltViewModel
class NewContactScreenViewModel @Inject constructor(
    logService: LogService
) : SmileViewModel(logService) {
    private val _uiState = MutableStateFlow(NewContactUiState())
    val uiState = _uiState.asStateFlow()


    fun onFirstNameChange(firstName: String) {
        _uiState.value = _uiState.value.copy(firstName = firstName.trim())
    }

    fun onLastNameChange(lastName: String) {
        _uiState.value = _uiState.value.copy(lastName = lastName.trim())
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email.trim())
    }

    fun onSaveClick() {
        uiState.value.apply {
            if (firstName.isBlank()) {
                SnackbarManager.showMessage(AppText.require_first_name)
                return
            }
            if (lastName.isBlank()) {
                SnackbarManager.showMessage(AppText.require_last_name)
                return
            }
            if (!email.isValidName()) {
                SnackbarManager.showMessage(AppText.email_error)
                return
            }
        }
        launchCatching {

        }
    }
}