package com.smile.ui.screens.login_screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.smile.R
import com.smile.common.composables.DefaultButton
import com.smile.common.composables.DefaultTextField
import com.smile.common.composables.ExtFloActionButton
import com.smile.common.composables.FormWrapper
import com.smile.common.composables.LoadingAnimationDialog
import com.smile.common.composables.LoginHeader
import com.smile.common.composables.OneTapSignUp
import com.smile.common.composables.PasswordTextField
import com.smile.common.composables.SignInUpWithGoogle
import com.smile.ui.screens.DayHeader
import com.smile.ui.screens.graph.SmileRoutes.FORGOT_PASSWORD_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
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
    openAndPopUp: (String) -> Unit,
    navigate: (String) -> Unit
) {
    LaunchedEffect(Unit) { viewModel.checkEmailVerification() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    if (uiState.loadingState) {
        LoadingAnimationDialog { viewModel.onLoadingStateChange(false) }
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
            onGoogleClick = { viewModel.oneTapSignIn() },
            onNotMemberClick = { openAndPopUp(REGISTER_SCREEN) },
            onForgotPasswordClick = { navigate(FORGOT_PASSWORD_SCREEN) }
        )
    }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credentials =
                        viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                    val googleIdToken = credentials.googleIdToken
                    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                    viewModel.signInWithGoogle(googleCredentials)
                } catch (it: ApiException) {
                    print(it)
                }
            }
        }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    OneTapSignUp(uiState.oneTapSignInResponse, launch = { launch(it) })

    SignInUpWithGoogle(uiState.signInWithGoogleResponse, navigateToHomeScreen = {
        openAndPopUp(HOME_SCREEN)
    })
}


@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onNotMemberClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
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
            Column {
                PasswordTextField(uiState.password, AppText.password, onPasswordChange)
                TextButton(
                    onClick = onForgotPasswordClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        stringResource(AppText.forgot_password)
                    )
                }
            }
            DefaultButton(text = AppText.login, onClick = onLoginClick, Modifier.fillMaxWidth())
        }
        DayHeader(
            stringResource(AppText.or),
            style = MaterialTheme.typography.titleMedium,
            height = VERY_HIGH_PADDING
        )
        ExtFloActionButton(
            R.drawable.google_32,
            AppText.google_icon,
            AppText.continue_google, onGoogleClick
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
    LoginScreen(LoginUiState("Fatih"), {}, {}, {}, {}, {}, {})
}