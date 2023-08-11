package com.smile.ui.view_models

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.model.Contact
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ContactScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _textFieldValue = MutableStateFlow(TextFieldValue(""))
    val textFieldValue = _textFieldValue.asStateFlow()

    private val _contacts = MutableStateFlow<List<List<Contact>>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun getContacts() {
        viewModelScope.launch {
            storageService.getContacts {
                _contacts.value = it
            }
        }
    }

    fun onTextFieldValueChange(textFieldValue: TextFieldValue) {
        _textFieldValue.value = textFieldValue
    }
}