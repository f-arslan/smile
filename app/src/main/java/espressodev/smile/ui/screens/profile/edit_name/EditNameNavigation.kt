package espressodev.smile.ui.screens.profile.edit_name

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val editNameRoute = "edit_name_route"

fun NavController.navigateToEditName(navOptions: NavOptions? = null) {
    navigate(editNameRoute, navOptions)
}

fun NavGraphBuilder.editNameScreen(popUp: () -> Unit) {
    composable(route = editNameRoute) {
        EditNameRoute(popUp)
    }
}