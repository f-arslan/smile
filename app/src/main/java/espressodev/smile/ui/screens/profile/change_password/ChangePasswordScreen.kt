package espressodev.smile.ui.screens.profile.change_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import espressodev.smile.common.composables.BottomButtonWithIcon
import espressodev.smile.common.composables.LoadingAnimationDialog
import espressodev.smile.common.composables.NavigationTopAppBar
import espressodev.smile.common.composables.PasswordTextField
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChangePasswordRoute(
    popUp: () -> Unit,
    clearAndNavigate: (String) -> Unit,
    viewModel: ChangePasswordScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    if (uiState.loadingState) {
        LoadingAnimationDialog { viewModel.onLoadingStateChange(false) }
    }
    ChangePasswordScreen(
        uiState,
        popUp = popUp,
        viewModel::onPasswordChange,
        viewModel::onRePasswordChange,
        onUpdateClick = {
            viewModel.onUpdateClick(clearAndNavigate)
            keyboardController?.hide()
        }
    )
}


@Composable
fun ChangePasswordScreen(
    uiState: ChangePasswordScreenUiState,
    popUp: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onRePasswordChange: (String) -> Unit,
    onUpdateClick: () -> Unit,
) {
    Scaffold(topBar = { NavigationTopAppBar(uiState.topBarLabel, popUp) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(HIGH_PADDING), verticalArrangement = Arrangement.spacedBy(HIGH_PADDING)
        ) {
            PasswordTextField(uiState.password, AppText.password, onPasswordChange)
            PasswordTextField(uiState.rePassword, AppText.re_password, onRePasswordChange)
            Spacer(Modifier.weight(1f))
            BottomButtonWithIcon(
                AppDrawable.outline_edit_24,
                AppText.edit_profile,
                AppText.update,
                onClick = onUpdateClick
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    ChangePasswordScreen(uiState = ChangePasswordScreenUiState(
        password = "congue",
        rePassword = "no",
        loadingState = false
    ),
        onPasswordChange = {},
        onRePasswordChange = {},
        popUp = {},
        onUpdateClick = {})
}