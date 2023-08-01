package com.smile.ui.view_models

import androidx.compose.ui.text.input.TextFieldValue
import com.smile.SmileViewModel
import com.smile.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject



@HiltViewModel
class ContactScreenViewModel @Inject constructor(
    logService: LogService
) : SmileViewModel(logService) {
    private val _textFieldValue = MutableStateFlow(TextFieldValue(""))
    val textFieldValue = _textFieldValue.asStateFlow()

    fun onTextFieldValueChange(textFieldValue: TextFieldValue) {
        _textFieldValue.value = textFieldValue
    }
}