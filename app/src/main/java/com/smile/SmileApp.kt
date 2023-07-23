package com.smile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.smile.ui.screens.RegisterScreenProvider
import com.smile.ui.screens.graph.SmileRoutes.ONBOARDING_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.REGISTER_SCREEN
import com.smile.ui.screens.graph.appGraph
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SmileApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val appState = rememberAppState()
        Scaffold(snackbarHost = { appState.snackbarHostState }) {
            NavHost(navController = appState.navController, startDestination = REGISTER_SCREEN) {
                appGraph(appState)
            }
        }
    }
}


@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(navController, snackbarHostState, coroutineScope) {
    SmileAppState(navController, snackbarHostState, coroutineScope)
}