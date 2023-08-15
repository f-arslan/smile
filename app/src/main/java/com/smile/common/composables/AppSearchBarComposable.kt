package com.smile.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.smile.util.Constants.MEDIUM_HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    userLetter: String,
    query: String,
    isActive: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onMenuClick: () -> Unit
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = isActive,
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
            IconButton(onClick = {}) { UserAvatar(letter = userLetter) }
        },
        leadingIcon = {
            DefaultIconButton(
                AppDrawable.baseline_menu_24,
                AppText.menu,
                onMenuClick
            )
        },
        modifier = Modifier.fillMaxWidth().padding(MEDIUM_PADDING)
    ) {

    }
}


@Composable
@Preview(showBackground = true)
fun AppSearchBarPreview() {
    AppSearchBar(
        "A",
        query = "",
        isActive = false,
        onQueryChange = {},
        onSearch = {},
        onActiveChange = {}, {})
}