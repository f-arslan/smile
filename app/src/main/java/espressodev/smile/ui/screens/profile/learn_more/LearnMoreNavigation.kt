package espressodev.smile.ui.screens.profile.learn_more

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val learnMoreRoute = "learn_more_route"

fun NavController.navigateToLearnMore(navOptions: NavOptions? = null) {
    navigate(learnMoreRoute, navOptions)
}

fun NavGraphBuilder.learnMoreScreen(popUp: () -> Unit) {
    composable(route = learnMoreRoute) {
        LearnMoreRoute(popUp)
    }
}