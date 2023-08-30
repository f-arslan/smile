package com.smile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.smile.R
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import com.smile.ui.view_models.SplashScreenViewModel


@Composable
fun SplashScreenProvider(
    clearAndNavigate: (String) -> Unit,
    viewModel: SplashScreenViewModel = hiltViewModel()
) {
    val isEmailVerified = viewModel.isEmailVerified
    val logoTheme = if (isSystemInDarkTheme()) R.raw.logo_black_theme else R.raw.logo_white_theme
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(logoTheme))
    SplashScreen(clearAndNavigate, isEmailVerified, composition)
}

@Composable
fun SplashScreen(popUpAndNavigate: (String) -> Unit, isEmailVerified: Boolean, composition: LottieComposition?) {
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
            popUpAndNavigate(if (isEmailVerified) HOME_SCREEN else LOGIN_SCREEN)
        }
    }
}