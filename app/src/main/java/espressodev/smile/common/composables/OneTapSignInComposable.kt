package espressodev.smile.common.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.identity.BeginSignInResult
import espressodev.smile.data.google.OneTapSignInUpResponse
import espressodev.smile.data.google.SignInUpWithGoogleResponse
import espressodev.smile.data.service.model.GoogleResponse.Failure
import espressodev.smile.data.service.model.GoogleResponse.Loading
import espressodev.smile.data.service.model.GoogleResponse.Success


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