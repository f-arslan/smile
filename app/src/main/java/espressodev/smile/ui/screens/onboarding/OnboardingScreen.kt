package espressodev.smile.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import espressodev.smile.common.composables.DefaultButton
import espressodev.smile.common.composables.DefaultOutlinedButton
import espressodev.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.REGISTER_SCREEN
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.MAX_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText

@Composable
fun OnboardingRoute(
    clearAndNavigate: (String) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.setOnboardingScreenState() }
    OnBoardingScreen(clearAndNavigate)
}

@Composable
fun OnBoardingScreen(popUpAndNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(AppDrawable.welcome_banner),
            contentDescription = stringResource(AppText.smile_icon),
            modifier = Modifier.size(500.dp),
            contentScale = ContentScale.Fit
        )
        OnboardingButtons(
            onLoginClick = { popUpAndNavigate(LOGIN_SCREEN) },
            onRegisterClick = { popUpAndNavigate(REGISTER_SCREEN) }
        )
    }
}


@Composable
fun OnboardingButtons(onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    Column(
        modifier = Modifier
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