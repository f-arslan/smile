package espressodev.smile.ui.screens.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val chatRoute = "chat_route"

fun NavController.navigateToChat(navOptions: NavOptions? = null) {
    navigate(chatRoute, navOptions)
}

fun NavGraphBuilder.chatScreen(popUp: () -> Unit) {
    composable(
        route = "$chatRoute/{contactId}/{roomId}",
        arguments = listOf(
            navArgument("contactId") { type = NavType.StringType },
            navArgument("roomId") { type = NavType.StringType }
        )
    ) {
        ChatRoute(
            contactId = it.arguments?.getString("contactId") ?: "",
            roomId = it.arguments?.getString("roomId") ?: "",
            popUp = popUp
        )
    }
}