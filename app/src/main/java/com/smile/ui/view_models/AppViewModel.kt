package com.smile.ui.view_models

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.smile.SmileViewModel
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class AppViewModel @Inject constructor(
    logService: LogService
) : SmileViewModel(logService) {



}