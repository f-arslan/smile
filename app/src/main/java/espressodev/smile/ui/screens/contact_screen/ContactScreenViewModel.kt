package espressodev.smile.ui.screens.contact_screen

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.data.room.ContactEntity
import espressodev.smile.data.service.LogService
import espressodev.smile.data.service.StorageService
import espressodev.smile.domain.util.turnListToGroupByLetter
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

    private val _contacts = MutableStateFlow<List<List<ContactEntity>>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun getContacts() {
        viewModelScope.launch {
            storageService.getContacts(viewModelScope) {
                Log.d("ContactScreenViewModel", "getContacts: $it")
                _contacts.value = turnListToGroupByLetter(it).reversed()
            }
        }
    }

    fun onTextFieldValueChange(textFieldValue: TextFieldValue) {
        _textFieldValue.value = textFieldValue
    }
}