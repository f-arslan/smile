package com.smile.ui.view_models

import com.smile.SmileViewModel
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    logService: LogService
): SmileViewModel(logService) {

}

