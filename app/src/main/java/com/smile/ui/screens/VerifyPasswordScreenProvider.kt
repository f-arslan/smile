package com.smile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.LoadingAnimationDialog
import com.smile.common.composables.NavigationTopAppBar
import com.smile.common.composables.PasswordTextField
import com.smile.ui.view_models.VerifyPasswordScreenViewModel
import com.smile.util.Constants.GENERAL_ICON_SIZE
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.SMALL_PADDING
import com.smile.util.keyboardAsState
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun VerifyPasswordScreenProvider(
    popUp: () -> Unit,
    navigate: (String) -> Unit,
    viewModel: VerifyPasswordScreenViewModel = hiltViewModel()
) {
    val password by viewModel.password.collectAsStateWithLifecycle()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    if (loadingState) {
        LoadingAnimationDialog { viewModel.onLoadingStateChange(false) }
    }
    VerifyPasswordScreen(
        popUp,
        password,
        viewModel::onPasswordChange,
        onConfirmClick = { viewModel.onConfirmClick { navigate(it) } }
    )
}


@Composable
fun VerifyPasswordScreen(
    popUp: () -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onConfirmClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val keyboard by keyboardAsState()
    LaunchedEffect(keyboard) {
        scrollState.animateScrollTo(Int.MAX_VALUE)
    }
    Scaffold(topBar = { NavigationTopAppBar(AppText.confirm_password, popUp) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(start = HIGH_PADDING, end = HIGH_PADDING, bottom = HIGH_PADDING)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val image =
                if (isSystemInDarkTheme()) AppDrawable.password_black_theme else AppDrawable.password_white_theme
            Image(
                painter = painterResource(image),
                contentDescription = stringResource(AppText.password),
                Modifier.size(GENERAL_ICON_SIZE),
            )
            Text(
                stringResource(AppText.password_insurance),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = SMALL_PADDING, end = SMALL_PADDING)
            )
            Spacer(modifier = Modifier.height(HIGH_PADDING))
            PasswordTextField(password, AppText.password, onPasswordChange, onConfirmClick)
            Spacer(modifier = Modifier.imePadding())
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PrePreview() {
    VerifyPasswordScreen(popUp = {}, password = "purus", onPasswordChange = {}, {})
}