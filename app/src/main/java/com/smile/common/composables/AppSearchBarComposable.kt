package com.smile.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smile.model.User
import com.smile.model.room.HomeContactEntity
import com.smile.model.room.SearchHistoryQueryEntity
import com.smile.model.service.module.Response
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AppSearchBar(
    userResponse: Response<User>,
    query: String,
    isActive: Boolean,
    searchHistoryQueriesResponse: Response<List<SearchHistoryQueryEntity>>,
    searchHistoryContactsResponse: Response<List<HomeContactEntity>>,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onMenuClick: () -> Unit,
    onAvatarClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
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
            placeholder = {
                Row(horizontalArrangement = Arrangement.spacedBy(MEDIUM_PADDING)) {
                    Icon(
                        painter = painterResource(id = AppDrawable.baseline_search_24),
                        contentDescription = stringResource(id = AppText.search)
                    )
                    Text(text = stringResource(id = AppText.search_messages))
                }
            },
            trailingIcon = {
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
            },
            leadingIcon = {
                DefaultIconButton(
                    AppDrawable.baseline_menu_24,
                    AppText.menu,
                    onMenuClick
                )
            },
            modifier = Modifier
                .align(Alignment.Center)
                .semantics {
                    traversalIndex = -1f
                }
        ) {
            if (searchHistoryContactsResponse is Response.Success && searchHistoryQueriesResponse is Response.Success) {
                LazyColumn(
                    contentPadding = PaddingValues(HIGH_PADDING),
                    verticalArrangement = Arrangement.spacedBy(
                        MEDIUM_PADDING
                    )
                ) {
                    items(searchHistoryQueriesResponse.data) {
                        SearchHistoryItem(query = it.query)
                    }
                    if (searchHistoryContactsResponse.data.isNotEmpty())
                        item { Divider() }
                    items(searchHistoryContactsResponse.data) {
                        SearchHistoryItem(
                            query = "${it.firstName} ${it.lastName}",
                            isHistory = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchHistoryItem(query: String, isHistory: Boolean = true) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING),
        horizontalArrangement = Arrangement.spacedBy(
            HIGH_PADDING
        )
    ) {
        if (isHistory)
            Icon(
                painter = painterResource(id = AppDrawable.outline_history_24),
                contentDescription = stringResource(
                    id = AppText.history
                )
            )
        Text(text = query, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleSearchBar() {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = -1f },
            query = text,
            onQueryChange = { text = it },
            onSearch = { active = false },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = { Text("Hinted search text") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
        ) {
            repeat(4) { idx ->
                val resultText = "Suggestion $idx"
                ListItem(
                    headlineContent = { Text(resultText) },
                    supportingContent = { Text("Additional info") },
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                    modifier = Modifier
                        .clickable {
                            text = resultText
                            active = false
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val list = List(100) { "Text $it" }
            items(count = list.size) {
                Text(
                    list[it],
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
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
        onActiveChange = {}, {}, {})
}