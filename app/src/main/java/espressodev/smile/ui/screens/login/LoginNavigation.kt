package espressodev.smile.ui.screens.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val LOGIN_GRAPH_ROUTE_PATTERN = "login_graph"
const val loginRoute = "login_route"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    this.navigate(loginRoute, navOptions)
}

fun NavGraphBuilder.loginGraph(
    clearAndNavigate: (String) -> Unit,
    navigate: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = LOGIN_GRAPH_ROUTE_PATTERN,
        startDestination = loginRoute
    ) {
        composable(route = loginRoute) {
            LoginRoute(clearAndNavigate, navigate)
        }
        nestedGraphs()
    }
}