package espressodev.smile.model.google.domain

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import espressodev.smile.model.service.module.GoogleResponse

typealias OneTapSignInUpResponse = GoogleResponse<BeginSignInResult>
typealias SignInUpWithGoogleResponse = GoogleResponse<Boolean>

interface AuthRepository {
    val isUserAuthenticatedInFirebase: Boolean

    suspend fun oneTapSignInWithGoogle(): OneTapSignInUpResponse

    suspend fun oneTapSignUpWithGoogle(): OneTapSignInUpResponse

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInUpWithGoogleResponse
}