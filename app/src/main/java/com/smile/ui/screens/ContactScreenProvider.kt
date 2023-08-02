package com.smile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.ContactTopAppBar
import com.smile.common.composables.NewContactButton
import com.smile.ui.view_models.ContactScreenViewModel

@Composable
fun ContactScreenProvider(
    popUp: () -> Unit,
    navigateNewContact: () -> Unit,
    viewModel: ContactScreenViewModel = hiltViewModel()
) {
    val textFieldValue by viewModel.textFieldValue.collectAsStateWithLifecycle()
    ContactScreen(textFieldValue, viewModel::onTextFieldValueChange, popUp, navigateNewContact)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    popUp: () -> Unit,
    navigateNewContact: () -> Unit
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ContactPreview() {
    NewContactButton(onClick = {})
}