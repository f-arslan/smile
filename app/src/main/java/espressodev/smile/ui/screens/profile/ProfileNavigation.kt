package espressodev.smile.ui.screens.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val PROFILE_GRAPH_ROUTE_PATTERN = "profile_graph"
const val profileRoute = "profile_route"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(profileRoute, navOptions)
}

fun NavGraphBuilder.profileGraph(
    popUp: () -> Unit,
    clearAndNavigate: (String) -> Unit,
    navigate: (String) -> Unit,
    navigateWithArgument: (String, String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = PROFILE_GRAPH_ROUTE_PATTERN,
        startDestination = profileRoute
    ) {
        composable(route = profileRoute) {
            ProfileRoute(popUp, clearAndNavigate, navigate, navigateWithArgument)
        }
        nestedGraphs()
    }
}
