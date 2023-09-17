package espressodev.smile

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import espressodev.smile.common.snackbar.SnackbarManager
import espressodev.smile.ui.screens.graph.SmileRoutes.SPLASH_SCREEN
import espressodev.smile.ui.screens.graph.appGraph
import espressodev.smile.util.Constants.MEDIUM_PADDING
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SmileApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val appState = rememberAppState()
        Scaffold(snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
                modifier = Modifier.padding(MEDIUM_PADDING),
                snackbar = { snackbarData ->
                    Snackbar(snackbarData = snackbarData)
                }
            )
        }) {
            NavHost(
                navController = appState.navController,
                startDestination = SPLASH_SCREEN,
            ) {
                appGraph(appState)
            }
        }
    }
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(navController, snackbarHostState, coroutineScope) {
    SmileAppState(navController, snackbarHostState, snackbarManager, resources, coroutineScope)
}


