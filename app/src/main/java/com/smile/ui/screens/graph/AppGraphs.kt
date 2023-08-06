package com.smile.ui.screens.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smile.SmileAppState
import com.smile.ui.screens.ChatScreenProvider
import com.smile.ui.screens.ContactScreenProvider
import com.smile.ui.screens.HomeScreenProvider
import com.smile.ui.screens.LoginScreenProvider
import com.smile.ui.screens.NewContactScreenProvider
import com.smile.ui.screens.OnBoardingScreen
import com.smile.ui.screens.RegisterScreenProvider
import com.smile.ui.screens.graph.SmileRoutes.CHAT_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.CONTACT_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.NEW_CONTACT_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.ONBOARDING_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.REGISTER_SCREEN

fun NavGraphBuilder.appGraph(appState: SmileAppState) {
    composable(ONBOARDING_SCREEN) {
        OnBoardingScreen { destination ->
            appState.clearAndNavigate(destination)
        }
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
        HomeScreenProvider { appState.navigate(CONTACT_SCREEN) }
    }

    composable(CONTACT_SCREEN) {
        ContactScreenProvider(
            popUp = { appState.popUp() },
            navigateNewContact = {
                appState.navigate(NEW_CONTACT_SCREEN)
            },
            navigateContactChat = {
                // Navigate with id parameter
                appState.navigate("$CHAT_SCREEN/$it")
            }
        )
    }

    composable(
        "$CHAT_SCREEN/{id}",
        arguments = listOf(navArgument("id") { type = NavType.StringType })
    ) {
        ChatScreenProvider(
            contactId = it.arguments?.getString("id") ?: "",
            popUp = { appState.popUp() })
    }

    composable(NEW_CONTACT_SCREEN) {
        NewContactScreenProvider(popUp = { appState.popUp() })
    }
}