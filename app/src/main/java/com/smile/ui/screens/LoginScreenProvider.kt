package com.smile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.R
import com.smile.common.composables.DefaultButton
import com.smile.common.composables.DefaultTextField
import com.smile.common.composables.ExtFloActionButton
import com.smile.common.composables.FormWrapper
import com.smile.common.composables.FunctionalityNotAvailablePopup
import com.smile.common.composables.LoadingAnimationDialog
import com.smile.common.composables.LoginHeader
import com.smile.common.composables.PasswordTextField
import com.smile.ui.screens.graph.SmileRoutes.REGISTER_SCREEN
import com.smile.ui.view_models.LoginScreenViewModel
import com.smile.ui.view_models.LoginUiState
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.VERY_HIGH_PADDING
import com.smile.R.string as AppText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreenProvider(
    viewModel: LoginScreenViewModel = hiltViewModel(),
    openAndPopUp: (String) -> Unit
) {
    LaunchedEffect(Unit) { viewModel.checkEmailVerification() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    if (loadingState) {
        LoadingAnimationDialog { viewModel.onLoadingStateChange(false) }
    }
    var notFunctionalState by remember { mutableStateOf(false) }
    if (notFunctionalState) {
        FunctionalityNotAvailablePopup { notFunctionalState = false }
    }
    Surface {
        LoginScreen(
            uiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = {
                keyboardController?.hide()
                viewModel.onLoginClick(openAndPopUp)
            },
            onGoogleClick = { notFunctionalState = true },
            onNotMemberClick = { openAndPopUp(REGISTER_SCREEN) }
        )
    }
}


@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onNotMemberClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = HIGH_PADDING),
        verticalArrangement = Arrangement.spacedBy(HIGH_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormWrapper {
            LoginHeader()
            DefaultTextField(uiState.email, AppText.email, onEmailChange)
            PasswordTextField(uiState.password, AppText.password, onPasswordChange)
            DefaultButton(text = AppText.login, onClick = onLoginClick, Modifier.fillMaxWidth())
        }
        DayHeader(stringResource(AppText.or), style = MaterialTheme.typography.titleMedium, height = VERY_HIGH_PADDING)
        ExtFloActionButton(
            R.drawable.google_32,
            AppText.google_icon,
            AppText.continue_google,
            onGoogleClick
        )
        Spacer(Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = HIGH_PADDING)
        ) {
            Text(
                text = stringResource(id = AppText.not_a_member),
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(onClick = onNotMemberClick) {
                Text(
                    text = stringResource(id = AppText.register_now),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(LoginUiState("Fatih"), {}, {}, {}, {}, {})
}