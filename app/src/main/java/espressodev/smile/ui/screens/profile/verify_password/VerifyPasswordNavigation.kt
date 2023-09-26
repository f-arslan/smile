package espressodev.smile.ui.screens.profile.verify_password

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val verifyPasswordRoute = "verify_password_route"

fun NavController.navigateToVerifyPassword(navOptions: NavOptions? = null) {
    navigate(verifyPasswordRoute, navOptions)
}

fun NavGraphBuilder.verifyPasswordScreen(
    popUp: () -> Unit,
    navigate: (String) -> Unit
) {
    composable(
        route = "$verifyPasswordRoute/{argument}",
        arguments = listOf(navArgument("argument") { type = NavType.StringType })
    ) {

        VerifyPasswordRoute(
            prevScreen = it.arguments?.getString("argument") ?: "",
            popUp = popUp,
            navigate = navigate
        )
    }
}