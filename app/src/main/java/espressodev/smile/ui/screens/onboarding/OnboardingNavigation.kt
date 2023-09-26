package espressodev.smile.ui.screens.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val onboardingRoute = "onboarding_route"

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    navigate(onboardingRoute, navOptions)
}

fun NavGraphBuilder.onboardingScreen(clearAndNavigate: (String) -> Unit) {
    composable(route = onboardingRoute) {
        OnboardingRoute(clearAndNavigate)
    }
}