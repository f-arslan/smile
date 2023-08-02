package com.smile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.BottomButton
import com.smile.common.composables.DefaultTextField
import com.smile.common.composables.NavigationTopAppBar
import com.smile.ui.view_models.NewContactScreenViewModel
import com.smile.ui.view_models.NewContactUiState
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.R.string as AppText

@Composable
fun NewContactScreenProvider(
    popUp: () -> Unit,
    viewModel: NewContactScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NewContactScreen(
        popUp,
        uiState,
        viewModel::onFirstNameChange,
        viewModel::onLastNameChange,
        viewModel::onEmailChange,
        viewModel::onPhoneNumberChange,
        viewModel::onSaveClick
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewContactScreen(
    popUp: () -> Unit,
    uiState: NewContactUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            NavigationTopAppBar(AppText.new_contact, popUp)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(vertical = MEDIUM_PADDING, horizontal = HIGH_PADDING)
                .imePadding()
                .imeNestedScroll()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(HIGH_PADDING),
        ) {
            DefaultTextField(uiState.firstName, AppText.first_name, onFirstNameChange)
            DefaultTextField(uiState.lastName, AppText.last_name, onLastNameChange)
            DefaultTextField(uiState.email, AppText.email, onEmailChange)
            Spacer(modifier = Modifier.weight(1f))
            BottomButton(
                text = AppText.save,
                onClick = onSaveClick,
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NewContactPreview() {
    NewContactScreen(popUp = {},
        uiState = NewContactUiState(
            firstName = "Robin Crosby",
            lastName = "Darryl Cummings",
            email = "lonnie.neal@example.com",
            phoneNumber = "(148) 671-6357"
        ),
        onFirstNameChange = {},
        onLastNameChange = {},
        onEmailChange = {},
        onPhoneNumberChange = {}, {})
}