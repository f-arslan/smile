package com.smile.common.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.smile.ui.view_models.ProfileScreenViewModel
import com.smile.model.service.module.GoogleResponse.*

@Composable
fun SignOut(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    navigateToAuthScreen: (signedOut: Boolean) -> Unit
) {
    when(val signOutResponse = viewModel.signOutResponse) {
        is Loading -> ProgressBar()
        is Success -> signOutResponse.data?.let { signedOut ->
            LaunchedEffect(signedOut) {
                navigateToAuthScreen(signedOut)
            }
        }
        is Failure -> LaunchedEffect(Unit) {
            print(signOutResponse.e)
        }
    }
}