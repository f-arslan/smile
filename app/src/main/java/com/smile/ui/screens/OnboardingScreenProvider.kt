package com.smile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.smile.common.composables.DefaultButton
import com.smile.common.composables.DefaultOutlinedButton
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.REGISTER_SCREEN
import com.smile.ui.theme.Pacifico
import com.smile.ui.view_models.OnboardingScreenViewModel
import com.smile.util.Constants
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MAX_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.util.Constants.NO_PADDING
import com.smile.util.Constants.SMALL_PADDING
import com.smile.util.Constants.VERY_MAX_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun OnboardingScreenProvider(
    clearAndNavigate: (String) -> Unit,
    viewModel: OnboardingScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.setOnboardingScreenState() }
    OnBoardingScreen(clearAndNavigate)
}

@Composable
fun OnBoardingScreen(popUpAndNavigate: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        OnboardingBanner(modifier = Modifier.weight(0.5f))
        OnboardingButtons(
            modifier = Modifier.weight(0.5f),
            onLoginClick = { popUpAndNavigate(LOGIN_SCREEN) },
            onRegisterClick = { popUpAndNavigate(REGISTER_SCREEN) }
        )
    }
}

@Composable
fun OnboardingBanner(modifier: Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NO_PADDING, NO_PADDING, VERY_MAX_PADDING, VERY_MAX_PADDING),
        shadowElevation = SMALL_PADDING
    ) {
        val logo = if (isSystemInDarkTheme()) AppDrawable.logo_white else AppDrawable.logo_black
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    MEDIUM_PADDING
                )
            ) {
                Image(
                    painter = painterResource(logo),
                    contentDescription = stringResource(AppText.logo),
                    modifier = Modifier.size(Constants.SMALL_ICON_SIZE)
                )
                Text(
                    text = stringResource(AppText.app_name),
                    style = MaterialTheme.typography.displayLarge,
                    fontFamily = Pacifico,
                )
            }
            Text(
                text = stringResource(AppText.onboarding_slogan),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun OnboardingButtons(modifier: Modifier, onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(HIGH_PADDING),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = AppText.onboarding_title),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(Modifier.height(MEDIUM_PADDING))
        Text(
            text = stringResource(id = AppText.onboarding_subtitle),
            style = MaterialTheme.typography.titleMedium,
            minLines = 2,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(MAX_PADDING))
        Row(horizontalArrangement = Arrangement.spacedBy(HIGH_PADDING)) {
            DefaultButton(AppText.login, onLoginClick)
            DefaultOutlinedButton(AppText.register, onRegisterClick)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewOnboarding() {
    OnBoardingScreen {}
}