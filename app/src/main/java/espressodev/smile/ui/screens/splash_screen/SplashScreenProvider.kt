package espressodev.smile.ui.screens.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import espressodev.smile.R
import espressodev.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.ONBOARDING_SCREEN


@Composable
fun SplashScreenProvider(
    clearAndNavigate: (String) -> Unit,
    viewModel: SplashScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.getOnboardingScreenState() }
    val onboardingScreenState by viewModel.onboardingScreenState.collectAsStateWithLifecycle()
    val isEmailVerified = viewModel.isEmailVerified
    val logoTheme = if (isSystemInDarkTheme()) R.raw.logo_black_theme else R.raw.logo_white_theme
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(logoTheme))
    SplashScreen(clearAndNavigate, isEmailVerified, onboardingScreenState, composition)
}

@Composable
fun SplashScreen(
    clearAndNavigate: (String) -> Unit,
    isEmailVerified: Boolean,
    onboardingScreenState: Boolean,
    composition: LottieComposition?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val logoAnimationState =
            animateLottieCompositionAsState(composition = composition, speed = 1f)
        LottieAnimation(
            composition = composition,
            progress = { logoAnimationState.progress },
        )
        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
            if (!isEmailVerified) {
                if (!onboardingScreenState) clearAndNavigate(ONBOARDING_SCREEN)
                else clearAndNavigate(LOGIN_SCREEN)
            } else {
                clearAndNavigate(HOME_SCREEN)
            }
        }
    }
}