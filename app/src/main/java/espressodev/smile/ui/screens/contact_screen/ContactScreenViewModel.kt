package espressodev.smile.ui.screens.contact_screen

import dagger.hilt.android.lifecycle.HiltViewModel
import espressodev.smile.SmileViewModel
import espressodev.smile.common.ext.groupByLetter
import espressodev.smile.data.service.LogService
import espressodev.smile.data.service.StorageService
import espressodev.smile.data.service.model.Contact
import espressodev.smile.data.service.model.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ContactScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    logService: LogService
) : SmileViewModel(logService) {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _contacts = MutableStateFlow<Response<List<Contact>>>(Response.Loading)

    private val _allContacts =
        MutableStateFlow<Response<List<Pair<String, List<Contact>>>>>(Response.Loading)
    val allContacts = _allContacts.asStateFlow()

    fun onQueryChange(query: String) {
        if (query != _query.value) {
            _query.value = query
            filterContacts()
        }
    }

    fun getContacts() = launchCatching {
        storageService.getContacts().collect {
            val groupedContacts = it.groupByLetter()
            _contacts.value = Response.Success(it)
            _allContacts.value = Response.Success(groupedContacts)
        }
    }

    private fun filterContacts() {
        if (_contacts.value is Response.Success) {
            val allContactsData = (_contacts.value as Response.Success<List<Contact>>).data
            val filteredContacts = if (query.value.isNotEmpty()) {
                allContactsData.filter {
                    it.firstName.contains(query.value, true) || it.lastName.contains(
                        query.value,
                        true
                    )
                }
            } else {
                allContactsData
            }
            val groupedContacts = filteredContacts.groupByLetter()
            _allContacts.value = Response.Success(groupedContacts)
        }
    }
}
