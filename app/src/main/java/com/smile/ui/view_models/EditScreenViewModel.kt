package com.smile.ui.view_models

import androidx.annotation.StringRes
import com.smile.SmileViewModel
import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.smile.R.string as AppText

data class EditScreenUiState(
    val name: String = "",
    val password: String = "",
    val rePassword: String = "",
    @StringRes val topBarLabel: Int = AppText.edit_profile,
    val loadingState: Boolean = false
)

@HiltViewModel
class EditScreenViewModel @Inject constructor(
    storageService: StorageService,
    accountService: AccountService,
    logService: LogService
) : SmileViewModel(logService) {

    private val _uiState = MutableStateFlow(EditScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(name = newName)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onRePasswordChange(newRePassword: String) {
        _uiState.value = _uiState.value.copy(rePassword = newRePassword)
    }

    fun onUpdateClick() {

    }

}