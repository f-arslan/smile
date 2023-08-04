package com.smile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.smile.R
import com.smile.common.composables.DefaultButton
import com.smile.common.composables.DefaultTextField
import com.smile.common.composables.FloAppButton
import com.smile.common.composables.FormWrapper
import com.smile.common.composables.LoginHeader
import com.smile.common.composables.PasswordTextField
import com.smile.ui.screens.graph.SmileRoutes.REGISTER_SCREEN
import com.smile.ui.view_models.LoginScreenViewModel
import com.smile.ui.view_models.LoginUiState
import com.smile.util.Constants.HIGH_PADDING
import com.smile.R.string as AppText

@Composable
fun LoginScreenProvider(
    viewModel: LoginScreenViewModel = hiltViewModel(),
    openAndPopUp: (String) -> Unit
) {
    val uiState = viewModel.uiState
    LaunchedEffect(Unit) { viewModel.checkEmailVerification() }
    val interactionSource = MutableInteractionSource()
    Surface(
        modifier = Modifier.fillMaxSize().clickable(interactionSource,  null) {
            viewModel.checkEmailVerification()
        }
    ) {
        LoginScreen(
            uiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = { viewModel.onLoginClick(openAndPopUp) },
            onGoogleClick = viewModel::onGoogleLoginClick,
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
        modifier = Modifier.statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(HIGH_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginHeader()
        FormWrapper {
            DefaultTextField(uiState.email, AppText.email, onEmailChange)
            PasswordTextField(uiState.password, AppText.password, onPasswordChange)
            DefaultButton(text = AppText.login, onClick = onLoginClick, Modifier.fillMaxWidth())
        }
        Text(
            text = stringResource(id = AppText.or_continue_with),
            style = MaterialTheme.typography.titleMedium
        )
        FloAppButton(R.drawable.google_32, AppText.google_icon, onGoogleClick)
        Row(
            verticalAlignment = Alignment.CenterVertically
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