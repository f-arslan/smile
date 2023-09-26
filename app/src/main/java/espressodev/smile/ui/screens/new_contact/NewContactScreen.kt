package espressodev.smile.ui.screens.new_contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import espressodev.smile.common.composables.BottomButton
import espressodev.smile.common.composables.DefaultTextField
import espressodev.smile.common.composables.LoadingAnimationDialog
import espressodev.smile.common.composables.NavigationTopAppBar
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING
import espressodev.smile.R.string as AppText

@Composable
fun NewContactRoute(
    popUp: () -> Unit,
    viewModel: NewContactScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    if (loadingState) {
        LoadingAnimationDialog { viewModel.onLoadingStateChange(false) }
    }
    NewContactScreen(
        popUp,
        uiState,
        viewModel::onFirstNameChange,
        viewModel::onLastNameChange,
        viewModel::onEmailChange,
        onSaveClick = { viewModel.onSaveClick { popUp() } }
    )
}

@Composable
fun NewContactScreen(
    popUp: () -> Unit,
    uiState: NewContactUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            NavigationTopAppBar(AppText.new_contact, popUp)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(vertical = MEDIUM_PADDING, horizontal = HIGH_PADDING),
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
        ),
        onFirstNameChange = {},
        onLastNameChange = {},
        onEmailChange = {},
        {})
}