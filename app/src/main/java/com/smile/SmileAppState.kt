package com.smile

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class SmileAppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    val coroutineScope: CoroutineScope
) {

    init {
        coroutineScope.launch {
            snackbarHostState.showSnackbar("Hello World")
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

    fun navigateWithArgument(route: String, argument: String) {
        navController.navigate("$route/$argument") { launchSingleTop = true }
    }
}