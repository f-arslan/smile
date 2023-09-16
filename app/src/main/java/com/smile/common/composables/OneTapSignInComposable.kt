package com.smile.common.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.smile.model.google.domain.OneTapSignInUpResponse
import com.smile.model.google.domain.SignInUpWithGoogleResponse
import com.smile.model.service.module.GoogleResponse.Failure
import com.smile.model.service.module.GoogleResponse.Loading
import com.smile.model.service.module.GoogleResponse.Success


@Composable
fun OneTapSignInUp(
    oneTapSignUpResponse: OneTapSignInUpResponse,
    launch: (result: BeginSignInResult) -> Unit
) {
    when (oneTapSignUpResponse) {
        is Loading -> ProgressBar()
        is Success -> oneTapSignUpResponse.data?.let {
            LaunchedEffect(it) {
                launch(it)
            }
        }
        is Failure -> LaunchedEffect(Unit) {
            Log.d("OneTapSignUp", oneTapSignUpResponse.e.toString())
        }
    }
}

@Composable
fun ProgressBar() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun SignInUpWithGoogle(
    signUpWithGoogleResponse: SignInUpWithGoogleResponse,
    navigateToHomeScreen: (signedIn: Boolean) -> Unit
) {
    when (signUpWithGoogleResponse) {
        is Loading -> ProgressBar()
        is Success -> signUpWithGoogleResponse.data?.let { signedIn ->
            LaunchedEffect(signedIn) {
                navigateToHomeScreen(signedIn)
            }
        }
        is Failure -> LaunchedEffect(Unit) {
            Log.d("SignUpWithGoogle", signUpWithGoogleResponse.e.toString())
        }
    }
}