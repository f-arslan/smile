package espressodev.smile.ui.screens.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val registerRoute = "register_route"

fun NavController.navigateToRegister(navOptions: NavOptions? = null) {
    navigate(registerRoute, navOptions)
}

fun NavGraphBuilder.registerScreen(clearAndNavigate: (String) -> Unit) {
    composable(route = registerRoute) {
        RegisterRoute(clearAndNavigate)
    }
}