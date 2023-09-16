package com.smile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.ContactTopAppBar
import com.smile.common.composables.LetterInCircle
import com.smile.common.composables.NewContactButton
import com.smile.model.room.ContactEntity
import com.smile.ui.view_models.ContactScreenViewModel
import com.smile.util.Constants.AVATAR_SIZE
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.HIGH_PLUS_PADDING
import com.smile.util.Constants.MEDIUM_HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING

@Composable
fun ContactScreenProvider(
    popUp: () -> Unit,
    navigateNewContact: () -> Unit,
    navigateChatScreen: (String, String) -> Unit,
    viewModel: ContactScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getContacts()
    }
    val textFieldValue by viewModel.textFieldValue.collectAsStateWithLifecycle()
    val groupedContacts by viewModel.contacts.collectAsStateWithLifecycle()
    ContactScreen(
        textFieldValue,
        groupedContacts,
        viewModel::onTextFieldValueChange,
        popUp,
        navigateNewContact,
        navigateChatScreen = navigateChatScreen
    )
}

@Composable
private fun ContactScreen(
    textFieldValue: TextFieldValue,
    groupContacts: List<List<ContactEntity>>,
    onValueChange: (TextFieldValue) -> Unit,
    popUp: () -> Unit,
    navigateNewContact: () -> Unit,
    navigateChatScreen: (String, String) -> Unit
) {
    Scaffold(
        topBar = {
            ContactTopAppBar(textFieldValue, onValueChange, popUp)
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            NewContactButton(navigateNewContact)
            LazyColumn {
                groupContacts.forEach {
                    item { Letter(it[0].firstName.first()) }
                    contactListWithLetter(
                        it,
                        navigateChatScreen
                    )
                    item { Spacer(Modifier.height(MEDIUM_PADDING)) }
                }
            }
        }
    }
}

private fun LazyListScope.contactListWithLetter(
    contacts: List<ContactEntity>,
    onContactClick: (String, String) -> Unit
) {
    items(contacts, key = { it.contactId }) {
        ContactItem(name = "${it.firstName} ${it.lastName}") {
            onContactClick(it.contactId, it.roomId)
        }
    }
}


@Composable
private fun Letter(label: Char) {
    Row {
        Spacer(Modifier.width(HIGH_PLUS_PADDING))
        Text(text = label.uppercase(), style = MaterialTheme.typography.titleSmall)
    }
}


@Composable
private fun ContactItem(name: String, onContactClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(HIGH_PADDING))
            .clickable { onContactClick() }
            .padding(MEDIUM_HIGH_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HIGH_PADDING)
    ) {
        LetterInCircle(letter = name.substring(0, 1), size = AVATAR_SIZE)
        Text(text = name, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
@Preview(showBackground = true)
private fun ContactPreview() {
    ContactScreen(
        textFieldValue = TextFieldValue("Hello"),
        groupContacts = listOf(),
        onValueChange = {},
        popUp = {},
        navigateNewContact = {},
        navigateChatScreen = { _: String, _: String -> })
}
