package espressodev.smile.ui.screens.profile.change_password

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val changePasswordRoute = "change_password_route"

fun NavController.navigateToChangePassword(navOptions: NavOptions? = null) {
    navigate(changePasswordRoute, navOptions)
}

fun NavGraphBuilder.changePasswordScreen(
    popUp: () -> Unit,
    clearAndNavigate: (String) -> Unit
) {
    composable(route = changePasswordRoute) {
        ChangePasswordRoute(popUp, clearAndNavigate)
    }
}