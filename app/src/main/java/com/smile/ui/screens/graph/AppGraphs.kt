package com.smile.ui.screens.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smile.SmileAppState
import com.smile.ui.screens.ChangePasswordScreenProvider
import com.smile.ui.screens.ChatScreenProvider
import com.smile.ui.screens.ContactScreenProvider
import com.smile.ui.screens.DeleteProfileScreenProvider
import com.smile.ui.screens.HomeScreenProvider
import com.smile.ui.screens.LearnMoreScreen
import com.smile.ui.screens.LoginScreenProvider
import com.smile.ui.screens.NameEditScreenProvider
import com.smile.ui.screens.NewContactScreenProvider
import com.smile.ui.screens.NotificationScreenProvider
import com.smile.ui.screens.OnboardingScreenProvider
import com.smile.ui.screens.ProfileScreenProvider
import com.smile.ui.screens.RegisterScreenProvider
import com.smile.ui.screens.SplashScreenProvider
import com.smile.ui.screens.VerifyPasswordScreenProvider
import com.smile.ui.screens.graph.SmileRoutes.CHANGE_PASSWORD_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.CHAT_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.CONTACT_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.DELETE_PROFILE_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.LEARN_MORE_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.NAME_EDIT_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.NEW_CONTACT_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.NOTIFICATION_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.ONBOARDING_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.PROFILE_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.REGISTER_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.SPLASH_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.VERIFY_PASSWORD_SCREEN

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.appGraph(appState: SmileAppState) {

    composable(ONBOARDING_SCREEN) {
        OnboardingScreenProvider(clearAndNavigate = { route -> appState.clearAndNavigate(route) })
    }
    composable(REGISTER_SCREEN) {
        RegisterScreenProvider { destination ->
            appState.clearAndNavigate(destination)
        }
    }
    composable(LOGIN_SCREEN) {
        LoginScreenProvider { destination -> appState.clearAndNavigate(destination) }
    }
    composable(HOME_SCREEN) {
        HomeScreenProvider(
            navigate = { appState.navigate(it) },
            navigateToChat = { contactId, roomId ->
                appState.navigate("$CHAT_SCREEN/$contactId/$roomId")
            },
            clearAndNavigate = { appState.clearAndNavigate(NOTIFICATION_SCREEN) }
        )
    }

    composable(CONTACT_SCREEN) {
        ContactScreenProvider(
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
        ChatScreenProvider(
            contactId = it.arguments?.getString("contactId") ?: "",
            roomId = it.arguments?.getString("roomId") ?: "",
            popUp = { appState.popUp() })
    }

    composable(NEW_CONTACT_SCREEN) {
        NewContactScreenProvider(popUp = { appState.popUp() })
    }

    composable(PROFILE_SCREEN) {
        ProfileScreenProvider(
            popUp = { appState.popUp() },
            clearAndNavigate = { appState.clearAndNavigate(LOGIN_SCREEN) },
            navigate = { appState.navigate(it) }
        )
    }

    composable(SPLASH_SCREEN) {
        SplashScreenProvider(clearAndNavigate = { appState.navigateAndPopUp(it, SPLASH_SCREEN) })
    }

    composable(NOTIFICATION_SCREEN) {
        NotificationScreenProvider(clearAndNavigate = { appState.clearAndNavigate(HOME_SCREEN) })
    }

    composable(CHANGE_PASSWORD_SCREEN) {
        ChangePasswordScreenProvider(popUp = { appState.popUp() },
            clearAndNavigate = { route -> appState.clearAndNavigate(route) })
    }

    composable(VERIFY_PASSWORD_SCREEN) {
        VerifyPasswordScreenProvider(
            popUp = { appState.popUp() },
            navigate = { route -> appState.navigate(route) })
    }

    composable(LEARN_MORE_SCREEN) {
        LearnMoreScreen { appState.popUp() }
    }

    composable(NAME_EDIT_SCREEN) {
        NameEditScreenProvider(popUp = { appState.popUp() })
    }

    composable(DELETE_PROFILE_SCREEN) {
        DeleteProfileScreenProvider(
            popUp = { appState.popUp() },
            clearAndNavigate = { route -> appState.clearAndNavigate(route) })
    }
}