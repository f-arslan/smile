package com.smile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.AppFloActionButton
import com.smile.common.composables.AppSearchBar
import com.smile.common.composables.FunctionalityNotAvailablePopup
import com.smile.common.composables.LetterInCircle
import com.smile.model.room.HomeContactEntity
import com.smile.model.service.module.Response
import com.smile.ui.view_models.HomeScreenViewModel
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MAX_PADDING
import com.smile.util.isTodayOrDate


@Composable
fun HomeScreenProvider(navigate: () -> Unit, viewModel: HomeScreenViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) { viewModel.getData() }
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val searchHistoryQueries by viewModel.searchHistoryQueries.collectAsStateWithLifecycle()
    val searchHistoryContacts by viewModel.searchHistoryContacts.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchIsActive by viewModel.searchIsActive.collectAsStateWithLifecycle()
    var notFunctionalState by remember { mutableStateOf(false) }
    if (notFunctionalState) {
        FunctionalityNotAvailablePopup { notFunctionalState = false }
    }
    Scaffold(
        floatingActionButton = { AppFloActionButton(onClick = navigate) },
        topBar = {
            AppSearchBar(
                userResponse = user,
                query = searchQuery,
                isActive = searchIsActive,
                searchHistoryQueriesResponse = searchHistoryQueries,
                searchHistoryContactsResponse = searchHistoryContacts,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearch = viewModel::onSearchClick,
                onActiveChange = viewModel::onSearchActiveChange,
                onMenuClick = {}) {

            }
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (val res = contacts) {
                Response.Loading -> {}
                is Response.Success -> {
                    LastContactList(data = res.data)
                }
                else -> {}
            }
        }
    }
}

@Composable
fun LastContactList(data: List<HomeContactEntity>) {
    LazyColumn {
        items(data, key = { it.contactId }) { contact ->
            LastContactItem(contact)
        }
    }
}

@Composable
fun LastContactItem(contact: HomeContactEntity) {
    Row(modifier = Modifier.padding(HIGH_PADDING)) {
        LetterInCircle(letter = contact.firstName.first().uppercase())
        Spacer(Modifier.width(HIGH_PADDING))
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = contact.firstName + " " + contact.lastName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.width(MAX_PADDING))
                Text(
                    text = isTodayOrDate(contact.timestamp.toLong()),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = contact.content,
                    maxLines = 2,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.width(MAX_PADDING))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LastContactPreview() {
    LastContactItem(
        HomeContactEntity(
            id = 4344,
            contactId = "dicunt",
            userId = "tota",
            friendId = "vivendo",
            firstName = "Mindy Lamb",
            lastName = "Jimmy Bradshaw",
            roomId = "mutat",
            content = "quidam",
            timestamp = "ornare"
        )
    )
}


