package com.smile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.smile.common.composables.DefaultApp
import com.smile.common.composables.DefaultButton
import com.smile.common.composables.DefaultTextField
import com.smile.common.composables.FormWrapper
import com.smile.common.composables.PasswordTextField
import com.smile.common.composables.RegisterHeader
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import com.smile.ui.view_models.RegisterScreenViewModel
import com.smile.ui.view_models.RegisterUiState
import com.smile.util.Constants.HIGH_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun RegisterScreenProvider(
    viewModel: RegisterScreenViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openAndPopUp: (String) -> Unit
) {
    val uiState = viewModel.uiState
    RegisterScreen(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onRePasswordChange,
        onSignUpClick = {
            viewModel.onSignUpClick {
                openAndPopUp(HOME_SCREEN)
            }
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
    Column(
        verticalArrangement = Arrangement.spacedBy(HIGH_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
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
        DefaultApp(AppDrawable.google_32, AppText.google_icon, onGoogleClick)
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