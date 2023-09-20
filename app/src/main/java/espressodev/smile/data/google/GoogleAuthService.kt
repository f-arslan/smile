package espressodev.smile.data.google

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import espressodev.smile.data.service.module.GoogleResponse

typealias OneTapSignInUpResponse = GoogleResponse<BeginSignInResult>
typealias SignInUpWithGoogleResponse = GoogleResponse<Boolean>

interface GoogleAuthService {

    suspend fun oneTapSignInWithGoogle(): OneTapSignInUpResponse

    suspend fun oneTapSignUpWithGoogle(): OneTapSignInUpResponse

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInUpWithGoogleResponse
}