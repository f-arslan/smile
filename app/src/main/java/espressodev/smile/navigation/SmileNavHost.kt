package espressodev.smile.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import espressodev.smile.SmileAppState
import espressodev.smile.ui.screens.chat.chatScreen
import espressodev.smile.ui.screens.contact.contactScreen
import espressodev.smile.ui.screens.home.homeScreen
import espressodev.smile.ui.screens.login.forgot_password.forgotPasswordScreen
import espressodev.smile.ui.screens.login.loginGraph
import espressodev.smile.ui.screens.new_contact.navigateToNewContact
import espressodev.smile.ui.screens.new_contact.newContactScreen
import espressodev.smile.ui.screens.onboarding.onboardingScreen
import espressodev.smile.ui.screens.profile.change_password.changePasswordScreen
import espressodev.smile.ui.screens.profile.delete_profile.deleteProfileScreen
import espressodev.smile.ui.screens.profile.edit_name.editNameScreen
import espressodev.smile.ui.screens.profile.learn_more.learnMoreScreen
import espressodev.smile.ui.screens.profile.notification.notificationScreen
import espressodev.smile.ui.screens.profile.profileGraph
import espressodev.smile.ui.screens.profile.verify_password.verifyPasswordScreen
import espressodev.smile.ui.screens.register.registerScreen
import espressodev.smile.ui.screens.splash.splashRoute
import espressodev.smile.ui.screens.splash.splashScreen


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SmileNavHost(
    appState: SmileAppState,
    modifier: Modifier = Modifier,
    startDestination: String = splashRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        chatScreen(popUp = appState::popUp)
        contactScreen(
            popUp = navController::popBackStack,
            navigateToNewContact = navController::navigateToNewContact,
            navigateChat = appState::navigateWithArgument
        )
        homeScreen(
            navigate = appState::navigate,
            navigateToChat = appState::navigateWithArgument,
            navigateToNotification = appState::clearAndNavigate
        )
        loginGraph(
            clearAndNavigate = appState::clearAndNavigate,
            navigate = appState::navigate,
            nestedGraphs = {
                forgotPasswordScreen(clearAndNavigate = appState::clearAndNavigate)
            }
        )
        newContactScreen(popUp = navController::popBackStack)
        onboardingScreen(clearAndNavigate = appState::clearAndNavigate)
        profileGraph(
            popUp = navController::popBackStack,
            clearAndNavigate = appState::clearAndNavigate,
            navigate = appState::navigate,
            navigateWithArgument = appState::navigateWithArgument,
            nestedGraphs = {
                changePasswordScreen(
                    popUp = navController::popBackStack,
                    clearAndNavigate = appState::clearAndNavigate
                )
                deleteProfileScreen(
                    popUp = navController::popBackStack,
                    clearAndNavigate = appState::clearAndNavigate
                )
                editNameScreen(popUp = navController::popBackStack)
                learnMoreScreen(popUp = navController::popBackStack)
                notificationScreen(clearAndNavigate = appState::clearAndNavigate)
                verifyPasswordScreen(
                    popUp = navController::popBackStack,
                    navigate = appState::navigate
                )
            }
        )
        registerScreen(clearAndNavigate = appState::clearAndNavigate)
        splashScreen(clearAndNavigate = appState::clearAndNavigate)
    }

}