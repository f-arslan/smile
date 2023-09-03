package com.smile.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.DefaultButton
import com.smile.common.composables.DefaultTextField
import com.smile.common.composables.FloAppButton
import com.smile.common.composables.FormWrapper
import com.smile.common.composables.LoadingAnimationDialog
import com.smile.common.composables.NavigationTopAppBar
import com.smile.common.composables.PasswordTextField
import com.smile.common.composables.RegisterHeader
import com.smile.common.composables.VerificationDialog
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import com.smile.ui.view_models.RegisterScreenViewModel
import com.smile.ui.view_models.RegisterUiState
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreenProvider(
    viewModel: RegisterScreenViewModel = hiltViewModel(),
    openAndPopUp: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.loadingState) {
        LoadingAnimationDialog { viewModel.onLoadingStateChange(false) }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    if (uiState.verificationState) {
        VerificationDialog {
            viewModel.onVerificationStateChange(false)
            openAndPopUp(LOGIN_SCREEN)
        }
    }
    RegisterScreen(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onRePasswordChange,
        onSignUpClick = {
            viewModel.onSignUpClick()
            keyboardController?.hide()
        },
        onGoogleClick = viewModel::onGoogleClick,
        onAlreadyHaveAccountClick = { openAndPopUp(LOGIN_SCREEN) }
    )
}


@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onAlreadyHaveAccountClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(MEDIUM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .statusBarsPadding()
            .verticalScroll(scrollState)
    ) {
        RegisterHeader()
        FormWrapper {
            DefaultTextField(uiState.name, AppText.name, onNameChange)
            DefaultTextField(uiState.email, AppText.email, onEmailChange)
            PasswordTextField(uiState.password, AppText.password, onPasswordChange)
            PasswordTextField(uiState.rePassword, AppText.re_password, onConfirmPasswordChange)
            DefaultButton(AppText.register, onSignUpClick, Modifier.fillMaxWidth())
        }
        Text(
            text = stringResource(id = AppText.or_continue_with),
            style = MaterialTheme.typography.titleMedium
        )
        FloAppButton(AppDrawable.google_32, AppText.google_icon, onGoogleClick)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = AppText.already_have_acc),
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(onClick = onAlreadyHaveAccountClick) {
                Text(
                    text = stringResource(id = AppText.login),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(RegisterUiState(), {}, {}, {}, {}, {}, {}, {})
}