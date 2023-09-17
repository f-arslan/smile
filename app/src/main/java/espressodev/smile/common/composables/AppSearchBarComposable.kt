package espressodev.smile.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import espressodev.smile.model.User
import espressodev.smile.model.room.ContactEntity
import espressodev.smile.model.room.SearchHistoryQueryEntity
import espressodev.smile.model.service.module.Response
import espressodev.smile.util.Constants.HIGH_PADDING
import espressodev.smile.util.Constants.MEDIUM_PADDING
import espressodev.smile.util.Constants.NO_PADDING
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AppSearchBar(
    userResponse: Response<User>,
    query: String,
    isActive: Boolean,
    searchHistoryQueriesResponse: Response<List<SearchHistoryQueryEntity>>,
    searchHistoryContactsResponse: Response<List<ContactEntity>>,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onAvatarClick: () -> Unit,
    onContactClick: (String, String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                start = if (isActive) NO_PADDING else MEDIUM_PADDING,
                end = if (isActive) NO_PADDING else MEDIUM_PADDING
            )
            .semantics {
                isTraversalGroup = true
            }
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            active = isActive,
            onSearch = {
                onSearch(it)
                keyboardController?.hide()
            },
            onActiveChange = onActiveChange,
            placeholder = { Text(text = stringResource(id = AppText.search_messages)) },
            trailingIcon = {
                if (!isActive)
                    IconButton(onClick = onAvatarClick) {
                        when (userResponse) {
                            is Response.Success -> {
                                val user = userResponse.data
                                val userLetter = user.displayName.first().toString()
                                UserAvatar(letter = userLetter)
                            }

                            else -> {}
                        }
                    }
                else
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            painter = painterResource(id = AppDrawable.outline_cancel_24),
                            contentDescription = stringResource(
                                id = AppText.cancel
                            )
                        )
                    }
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = AppDrawable.baseline_search_24),
                    contentDescription = stringResource(id = AppText.search)
                )
            },
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .semantics {
                    traversalIndex = -1f
                }
        ) {
            if (searchHistoryContactsResponse is Response.Success && searchHistoryQueriesResponse is Response.Success) {
                LazyColumn(
                    contentPadding = PaddingValues(MEDIUM_PADDING),
                    verticalArrangement = Arrangement.spacedBy(MEDIUM_PADDING)
                ) {
                    items(searchHistoryQueriesResponse.data) { queryEntity ->
                        SearchHistoryItem(
                            query = queryEntity.query,
                            onContactClick = {
                                onContactClick(
                                    queryEntity.contactId,
                                    queryEntity.roomId
                                )
                            })
                    }
                    if (searchHistoryQueriesResponse.data.isNotEmpty() && searchHistoryContactsResponse.data.isNotEmpty()) {
                        item { Divider() }
                    }
                    items(searchHistoryContactsResponse.data) { contactEntity ->
                        val contactName = "${contactEntity.firstName} ${contactEntity.lastName}"
                        SearchHistoryItem(
                            query = contactName,
                            isHistory = false,
                            onContactClick = {
                                onContactClick(
                                    contactEntity.contactId,
                                    contactEntity.roomId
                                )
                            })
                    }
                }
            }

        }
    }
}

@Composable
fun SearchHistoryItem(query: String, isHistory: Boolean = true, onContactClick: () -> Unit) {
    val icon = if (isHistory) AppDrawable.outline_history_24 else AppDrawable.outline_person_24
    val contentDescription = if (isHistory) AppText.history else AppText.contact
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(HIGH_PADDING))
            .clickable { onContactClick() }
            .padding(HIGH_PADDING),
        horizontalArrangement = Arrangement.spacedBy(
            HIGH_PADDING
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = contentDescription)
        )
        Text(text = query, fontWeight = FontWeight.SemiBold)
    }
}


@Composable
@Preview(showBackground = true)
fun AppSearchBarPreview() {
    AppSearchBar(
        userResponse = Response.Success(User(displayName = "Fatih")),
        query = "",
        isActive = false,
        searchHistoryQueriesResponse = Response.Loading,
        searchHistoryContactsResponse = Response.Loading,
        onQueryChange = {},
        onSearch = {},
        onActiveChange = {},  {}, { _, _ -> })
}