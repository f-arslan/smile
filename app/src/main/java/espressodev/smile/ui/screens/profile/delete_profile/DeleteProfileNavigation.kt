package espressodev.smile.ui.screens.profile.delete_profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val deleteProfileRoute = "delete_profile_route"

fun NavController.navigateToDeleteProfile(navOptions: NavOptions? = null) {
    navigate(deleteProfileRoute, navOptions)
}

fun NavGraphBuilder.deleteProfileScreen(
    popUp: () -> Unit,
    clearAndNavigate: (String) -> Unit
) {
    composable(route = deleteProfileRoute) {
        DeleteProfileRoute(popUp, clearAndNavigate)
    }
}
