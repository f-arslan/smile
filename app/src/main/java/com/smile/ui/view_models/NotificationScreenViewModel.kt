package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NotificationScreenViewModel @Inject constructor(logService: LogService) :
    SmileViewModel(logService) {
}