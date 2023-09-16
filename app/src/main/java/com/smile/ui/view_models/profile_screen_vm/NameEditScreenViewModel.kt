package com.smile.ui.view_models.profile_screen_vm

import com.smile.SmileViewModel
import com.smile.common.snackbar.SnackbarManager
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.module.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.smile.R.string as AppText

@HiltViewModel
class NameEditScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    logService: LogService
) : SmileViewModel(logService) {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    fun onLoadingStateChange(loadingState: Boolean) {
        _loadingState.value = loadingState
    }

    fun onNameChanged(name: String) {
        _name.value = name
    }

    fun onUpdateClick(popUp: () -> Unit) {
        onLoadingStateChange(true)
        if (_name.value.isBlank()) {
            SnackbarManager.showMessage(AppText.require_first_name)
            return
        }
        launchCatching {
            val updateUserNameResponse = storageService.updateUserName(_name.value.trim())
            if (updateUserNameResponse is Response.Success) {
                onLoadingStateChange(false)
                delay(100L)
                popUp()
            } else if (updateUserNameResponse is Response.Failure) {
                updateUserNameResponse.e.message?.let { SnackbarManager.showMessage(it) }
            }
            onLoadingStateChange(false)
        }
    }
}