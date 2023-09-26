package espressodev.smile.ui.screens.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val splashRoute = "splash_route"

fun NavController.navigateToSplash(navOptions: NavOptions? = null) {
    navigate(splashRoute, navOptions)
}

fun NavGraphBuilder.splashScreen(
    clearAndNavigate: (String) -> Unit
) {
    composable(route = splashRoute) {
        SplashRoute(clearAndNavigate)
    }
}