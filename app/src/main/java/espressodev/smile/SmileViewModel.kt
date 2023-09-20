package espressodev.smile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import espressodev.smile.common.snackbar.SnackbarManager
import espressodev.smile.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import espressodev.smile.data.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class SmileViewModel(private val logService: LogService): ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )


}