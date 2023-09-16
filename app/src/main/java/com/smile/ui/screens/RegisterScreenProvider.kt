package com.smile.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.smile.common.composables.DefaultButton
import com.smile.common.composables.DefaultTextField
import com.smile.common.composables.ExtFloActionButton
import com.smile.common.composables.FormWrapper
import com.smile.common.composables.HyperlinkText
import com.smile.common.composables.LoadingAnimationDialog
import com.smile.common.composables.OneTapSignInUp
import com.smile.common.composables.PasswordTextField
import com.smile.common.composables.RegisterHeader
import com.smile.common.composables.SignInUpWithGoogle
import com.smile.common.composables.VerificationDialog
import com.smile.model.service.module.LoadingState
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import com.smile.ui.view_models.RegisterScreenViewModel
import com.smile.ui.view_models.RegisterUiState
import com.smile.util.Constants
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.util.Constants.VERY_HIGH_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreenProvider(
    viewModel: RegisterScreenViewModel = hiltViewModel(),
    openAndPopUp: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.loadingState is LoadingState.Loading) {
        LoadingAnimationDialog { viewModel.onLoadingStateChange(LoadingState.Idle) }
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
        onGoogleClick = { viewModel.oneTapSignUp() },
        onAlreadyHaveAccountClick = { openAndPopUp(LOGIN_SCREEN) }
    )
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credentials =
                        viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                    val googleIdToken = credentials.googleIdToken
                    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                    viewModel.signUpWithGoogle(googleCredentials)
                } catch (it: ApiException) {
                    print(it)
                }
            }
        }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    OneTapSignInUp(
        oneTapSignUpResponse = uiState.oneTapSignUpResponse,
        launch = {
            launch(it)
        }
    )

    SignInUpWithGoogle(
        signUpWithGoogleResponse = uiState.signUpWithGoogleResponse,
        navigateToHomeScreen = { signedIn ->
            if (signedIn) {
                openAndPopUp(HOME_SCREEN)
            }
        }
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
            .fillMaxSize()
            .padding(top = HIGH_PADDING)
            .verticalScroll(scrollState)
    ) {
        FormWrapper {
            RegisterHeader()
            DefaultTextField(uiState.name, AppText.name, onNameChange)
            DefaultTextField(uiState.email, AppText.email, onEmailChange)
            PasswordTextField(uiState.password, AppText.password, onPasswordChange)
            PasswordTextField(uiState.rePassword, AppText.re_password, onConfirmPasswordChange)
            HyperlinkText(
                fullText = Constants.PRIVACY_POLICY_DESC, mapOf(
                    Constants.PRIVACY_POLICY.lowercase() to Constants.PRIVACY_POLICY_LINK,
                    Constants.TERMS_CONDITIONS.lowercase() to Constants.TERMS_CONDITIONS_LINK
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center)
            )
            DefaultButton(AppText.register, onSignUpClick, Modifier.fillMaxWidth())
        }
        DayHeader(
            stringResource(AppText.or),
            MaterialTheme.typography.titleMedium,
            height = VERY_HIGH_PADDING
        )
        ExtFloActionButton(
            AppDrawable.google_32,
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