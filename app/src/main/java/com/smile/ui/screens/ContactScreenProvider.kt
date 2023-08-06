package com.smile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.ContactTopAppBar
import com.smile.common.composables.NewContactButton
import com.smile.model.Contact
import com.smile.ui.view_models.ContactScreenViewModel
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.util.Constants.SMALL_PADDING

@Composable
fun ContactScreenProvider(
    popUp: () -> Unit,
    navigateNewContact: () -> Unit,
    navigateContactChat: (String) -> Unit,
    viewModel: ContactScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getContacts()
    }
    val textFieldValue by viewModel.textFieldValue.collectAsStateWithLifecycle()
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    ContactScreen(
        textFieldValue,
        contacts,
        viewModel::onTextFieldValueChange,
        popUp,
        navigateNewContact,
        navigateContactChat = navigateContactChat
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    textFieldValue: TextFieldValue,
    contacts: List<List<Contact>>,
    onValueChange: (TextFieldValue) -> Unit,
    popUp: () -> Unit,
    navigateNewContact: () -> Unit,
    navigateContactChat: (String) -> Unit
) {
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    Scaffold(
        topBar = {
            ContactTopAppBar(textFieldValue, onValueChange, popUp)
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            NewContactButton(navigateNewContact)
            for (contact in contacts) {
                ContactListWithLetter(contact[0].firstName.substring(0, 1), contact) {
                    navigateContactChat(it)
                }
            }
        }
    }
}

@Composable
fun ContactListWithLetter(
    label: String,
    contacts: List<Contact>,
    onContactClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = MEDIUM_PADDING, horizontal = MEDIUM_PADDING)
    ) {
        Row {
            Spacer(Modifier.width(20.dp))
            Text(text = label.uppercase(), style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.height(MEDIUM_PADDING))
        LazyColumn {
            items(contacts, key = { it.contactId }) {
                ContactItem(name = "${it.firstName} ${it.lastName}") {
                    onContactClick(it.contactId)
                }
            }
        }
    }
}


@Composable
fun ContactItem(name: String, onContactClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(HIGH_PADDING))
            .clickable { onContactClick() }
            .padding(horizontal = SMALL_PADDING, vertical = MEDIUM_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HIGH_PADDING)
    ) {
        ContactAvatar(letter = name.substring(0, 1))
        Text(text = name, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ContactAvatar(letter: String) {
    Surface(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.tertiary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = letter, style = MaterialTheme.typography.titleLarge)
        }
    }
}