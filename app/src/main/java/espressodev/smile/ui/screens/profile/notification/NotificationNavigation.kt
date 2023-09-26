package espressodev.smile.ui.screens.profile.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val notificationRoute = "notification_route"

fun NavController.navigateToNotification(navOptions: NavOptions? = null) {
    navigate(notificationRoute, navOptions)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.notificationScreen(clearAndNavigate: (String) -> Unit) {
    composable(route = notificationRoute) {
        NotificationRoute(clearAndNavigate)
    }
}