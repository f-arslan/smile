package espressodev.smile.ui.screens.new_contact

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val newContactRoute = "new_contact_route"

fun NavController.navigateToNewContact(navOptions: NavOptions? = null) {
    navigate(newContactRoute, navOptions)
}

fun NavGraphBuilder.newContactScreen(
    popUp: () -> Unit,
) {
    composable(route = newContactRoute) {
        NewContactRoute(popUp)
    }
}