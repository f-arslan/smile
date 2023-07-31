package com.smile.ui.view_models

import androidx.compose.runtime.mutableStateOf
import com.smile.SmileViewModel
import com.smile.model.Message
import com.smile.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    logService: LogService
) : SmileViewModel(logService) {


    fun addMessage(text: String) {

    }
}