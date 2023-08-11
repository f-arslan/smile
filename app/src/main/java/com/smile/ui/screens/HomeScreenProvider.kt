package com.smile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.smile.common.composables.AppFloActionButton
import com.smile.common.composables.AppSearchBar

@Composable
fun HomeScreenProvider(navigate: () -> Unit) {
    Scaffold(
        floatingActionButton = { AppFloActionButton(onClick = navigate) },
        topBar = {
            AppSearchBar(
                userLetter = "A", "A", false,
                {}, {}, {}, {}
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            LastContactList()
        }
    }
}

@Composable
fun LastContactList() {
}


