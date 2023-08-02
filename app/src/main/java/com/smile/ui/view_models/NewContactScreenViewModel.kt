package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class NewContactUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = ""
)

@HiltViewModel
class NewContactScreenViewModel @Inject constructor(
    logService: LogService
) : SmileViewModel(logService) {
    private val _uiState = MutableStateFlow(NewContactUiState())
    val uiState = _uiState.asStateFlow()


    fun onFirstNameChange(firstName: String) {
        _uiState.value = _uiState.value.copy(firstName = firstName)
    }

    fun onLastNameChange(lastName: String) {
        _uiState.value = _uiState.value.copy(lastName = lastName)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phoneNumber)
    }

    fun onSaveClick() {

    }
}