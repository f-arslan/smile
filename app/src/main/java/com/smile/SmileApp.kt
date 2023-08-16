package com.smile

import android.annotation.SuppressLint
import android.content.res.Resources
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.smile.common.snackbar.SnackbarManager
import com.smile.ui.screens.graph.SmileRoutes.HOME_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.LOGIN_SCREEN
import com.smile.ui.screens.graph.appGraph
import com.smile.ui.view_models.AppViewModel
import com.smile.util.Constants.MEDIUM_PADDING
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SmileApp(viewModel: AppViewModel = hiltViewModel()) {
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
            NavHost(navController = appState.navController, startDestination = LOGIN_SCREEN) {
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