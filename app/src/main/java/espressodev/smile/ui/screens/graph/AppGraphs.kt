package espressodev.smile.ui.screens.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import espressodev.smile.SmileAppState
import espressodev.smile.ui.screens.chat.ChatRoute
import espressodev.smile.ui.screens.contact.ContactRoute
import espressodev.smile.ui.screens.home.HomeRoute
import espressodev.smile.ui.screens.new_contact.NewContactRoute
import espressodev.smile.ui.screens.onboarding.OnboardingRoute
import espressodev.smile.ui.screens.register_screen.RegisterScreenProvider
import espressodev.smile.ui.screens.splash_screen.SplashScreenProvider
import espressodev.smile.ui.screens.graph.SmileRoutes.CHANGE_PASSWORD_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.CHAT_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.CONTACT_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.DELETE_PROFILE_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.FORGOT_PASSWORD_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.LEARN_MORE_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.NAME_EDIT_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.NEW_CONTACT_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.NOTIFICATION_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.ONBOARDING_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.PROFILE_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.REGISTER_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.SPLASH_SCREEN
import espressodev.smile.ui.screens.graph.SmileRoutes.VERIFY_PASSWORD_SCREEN
import espressodev.smile.ui.screens.login.forgot_password.ForgotPasswordRoute
import espressodev.smile.ui.screens.login.LoginRoute
import espressodev.smile.ui.screens.profile.change_password.ChangePasswordRoute
import espressodev.smile.ui.screens.profile.delete_profile.DeleteProfileRoute
import espressodev.smile.ui.screens.profile.learn_more.LearnMoreRoute
import espressodev.smile.ui.screens.profile.edit_name.EditNameRoute
import espressodev.smile.ui.screens.profile.notification.NotificationScreenProvider
import espressodev.smile.ui.screens.profile.ProfileRoute
import espressodev.smile.ui.screens.profile.verify_password_screen.VerifyPasswordScreenProvider

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.appGraph(appState: SmileAppState) {

    composable(ONBOARDING_SCREEN) {
        OnboardingRoute(clearAndNavigate = { route -> appState.clearAndNavigate(route) })
    }
    composable(REGISTER_SCREEN) {
        RegisterScreenProvider { destination ->
            appState.clearAndNavigate(destination)
        }
    }
    composable(LOGIN_SCREEN) {
        LoginRoute(navigate = {
            appState.navigate(FORGOT_PASSWORD_SCREEN)
        }, clearAndNavigate = { route ->
            appState.clearAndNavigate(route)
        })
    }
    composable(HOME_SCREEN) {
        HomeRoute(
            navigate = { appState.navigate(it) },
            navigateToChat = { contactId, roomId ->
                appState.navigate("$CHAT_SCREEN/$contactId/$roomId")
            },
            navigateToNotification = { appState.clearAndNavigate(NOTIFICATION_SCREEN) }
        )
    }

    composable(CONTACT_SCREEN) {
        ContactRoute(
            popUp = { appState.popUp() },
            navigateNewContact = {
                appState.navigate(NEW_CONTACT_SCREEN)
            },
            navigateChatScreen = { contactId, roomId ->
                appState.navigate("$CHAT_SCREEN/$contactId/$roomId")
            }
        )
    }

    composable(
        "$CHAT_SCREEN/{contactId}/{roomId}",
        arguments = listOf(
            navArgument("contactId") {
                type = NavType.StringType
            },
            navArgument("roomId") {
                type = NavType.StringType
            }
        ),
    ) {
        ChatRoute(
            contactId = it.arguments?.getString("contactId") ?: "",
            roomId = it.arguments?.getString("roomId") ?: "",
            popUp = { appState.popUp() })
    }

    composable(
        "$VERIFY_PASSWORD_SCREEN/{argument}",
        arguments = listOf(navArgument("argument") { type = NavType.StringType })
    ) {
        VerifyPasswordScreenProvider(
            prevScreen = it.arguments?.getString("argument") ?: "",
            popUp = { appState.popUp() },
            navigate = { route -> appState.navigate(route) })
    }

    composable(NEW_CONTACT_SCREEN) {
        NewContactRoute(popUp = { appState.popUp() })
    }

    composable(PROFILE_SCREEN) {
        ProfileRoute(
            popUp = { appState.popUp() },
            clearAndNavigate = { appState.clearAndNavigate(LOGIN_SCREEN) },
            navigate = { appState.navigate(it) },
            navigateWithArgument = { route, argument ->
                appState.navigateWithArgument(
                    route,
                    argument
                )
            }
        )
    }

    composable(SPLASH_SCREEN) {
        SplashScreenProvider(clearAndNavigate = { appState.navigateAndPopUp(it, SPLASH_SCREEN) })
    }

    composable(NOTIFICATION_SCREEN) {
        NotificationScreenProvider(clearAndNavigate = { appState.clearAndNavigate(HOME_SCREEN) })
    }

    composable(CHANGE_PASSWORD_SCREEN) {
        ChangePasswordRoute(popUp = { appState.popUp() },
            clearAndNavigate = { route -> appState.clearAndNavigate(route) })
    }


    composable(LEARN_MORE_SCREEN) {
        LearnMoreRoute { appState.popUp() }
    }

    composable(NAME_EDIT_SCREEN) {
        EditNameRoute(popUp = { appState.popUp() })
    }

    composable(DELETE_PROFILE_SCREEN) {
        DeleteProfileRoute(
            popUp = { appState.popUp() },
            clearAndNavigate = { route -> appState.clearAndNavigate(route) })
    }

    composable(FORGOT_PASSWORD_SCREEN) {
        ForgotPasswordRoute(clearAndNavigate = { route -> appState.clearAndNavigate(route) })
    }
}