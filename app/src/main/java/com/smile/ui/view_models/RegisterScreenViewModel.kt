package com.smile.ui.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.smile.SmileViewModel


data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val rePassword: String = ""
)

class RegisterScreenViewModel: SmileViewModel() {
    private var _uiState by mutableStateOf(RegisterUiState())

    val uiState: RegisterUiState
        get() = _uiState


    fun onNameChange(newValue: String) {
        _uiState = _uiState.copy(name = newValue)
    }

    fun onEmailChange(newValue: String) {
        _uiState = _uiState.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState = _uiState.copy(password = newValue)
    }

    fun onRePasswordChange(newValue: String) {
        _uiState = _uiState.copy(rePassword = newValue)
    }

    fun onSignUpClick() {

    }

    fun onGoogleClick() {

    }

}