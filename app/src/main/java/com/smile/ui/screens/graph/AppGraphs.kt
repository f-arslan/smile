package com.smile.ui.screens.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smile.SmileAppState
import com.smile.ui.screens.HomeScreenProvider
import com.smile.ui.screens.LoginScreenProvider
import com.smile.ui.screens.OnBoardingScreen
import com.smile.ui.screens.RegisterScreenProvider
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.ONBOARDING_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.REGISTER_SCREEN

fun NavGraphBuilder.appGraph(appState: SmileAppState) {
    composable(ONBOARDING_SCREEN) {
        OnBoardingScreen()
    }
    composable(REGISTER_SCREEN)  {
        RegisterScreenProvider(snackbarHostState = appState.snackbarHostState) {
            appState.clearAndNavigate(HOME_SCREEN)
        }
    }
    composable(LOGIN_SCREEN) {
        LoginScreenProvider()
    }
    composable(HOME_SCREEN) {
        HomeScreenProvider()
    }
}