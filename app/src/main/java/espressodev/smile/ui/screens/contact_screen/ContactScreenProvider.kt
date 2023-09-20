package espressodev.smile.ui.screens.contact_screen

import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import espressodev.smile.common.composables.ContactTopAppBar
import espressodev.smile.common.composables.LetterInCircle
import espressodev.smile.common.composables.NewContactButton
import espressodev.smile.data.service.model.Contact
import espressodev.smile.data.service.model.Response
import espressodev.smile.domain.util.Constants.AVATAR_SIZE
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.HIGH_PLUS_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_HIGH_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING

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
    val query by viewModel.query.collectAsStateWithLifecycle()
    val groupedContacts by viewModel.allContacts.collectAsStateWithLifecycle()
    Log.d("ContactScreenProvider", "groupedContacts: $groupedContacts")
    ContactScreen(
        query,
        groupedContacts,
        viewModel::onQueryChange,
        popUp,
        navigateNewContact,
        navigateChatScreen = navigateChatScreen
    )
}

@Composable
private fun ContactScreen(
    query: String,
    groupContacts: Response<List<Pair<String, List<Contact>>>>,
    onValueChange: (String) -> Unit,
    popUp: () -> Unit,
    navigateNewContact: () -> Unit,
    navigateChatScreen: (String, String) -> Unit
) {
    Scaffold(
        topBar = { ContactTopAppBar(query, onValueChange, popUp) },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            NewContactButton(navigateNewContact)
            when (groupContacts) {
                is Response.Success -> {
                    LazyColumn {
                        groupContacts.data.forEach {
                            item { Letter(it.first.first()) }
                            contactListWithLetter(it.second, navigateChatScreen)
                            item { Spacer(Modifier.height(MEDIUM_PADDING)) }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

private fun LazyListScope.contactListWithLetter(
    contacts: List<Contact>,
    onContactClick: (String, String) -> Unit
) {
    items(contacts) {
        ContactItem(name = it.toFullName()) {
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
        query = "dapibus",
        groupContacts = Response.Success(listOf()),
        onValueChange = {},
        popUp = {},
        navigateNewContact = {},
        navigateChatScreen = { _: String, _: String -> })
}
