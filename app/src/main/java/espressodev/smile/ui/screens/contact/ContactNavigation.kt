package espressodev.smile.ui.screens.contact

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val contactRoute = "contact_route"

fun NavController.navigateToContact(navOptions: NavOptions? = null) {
    navigate(contactRoute, navOptions)
}

fun NavGraphBuilder.contactScreen(
    popUp: () -> Unit,
    navigateToNewContact: () -> Unit,
    navigateChat: (String, String) -> Unit
) {
    composable(route = contactRoute) {
        ContactRoute(popUp, navigateToNewContact, navigateChat)
    }
}