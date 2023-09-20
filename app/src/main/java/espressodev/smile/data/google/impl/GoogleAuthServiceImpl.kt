package espressodev.smile.data.google.impl

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import espressodev.smile.data.service.model.PROVIDER
import espressodev.smile.data.service.model.User
import espressodev.smile.data.google.GoogleAuthService
import espressodev.smile.data.google.OneTapSignInUpResponse
import espressodev.smile.data.google.SignInUpWithGoogleResponse
import espressodev.smile.data.service.StorageService
import espressodev.smile.data.service.model.GoogleResponse.Failure
import espressodev.smile.data.service.model.GoogleResponse.Success
import espressodev.smile.domain.util.Constants.SIGN_IN_REQUEST
import espressodev.smile.domain.util.Constants.SIGN_UP_REQUEST
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class GoogleAuthServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val storageService: StorageService,
) : GoogleAuthService {
    override suspend fun oneTapSignInWithGoogle(): OneTapSignInUpResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Success(signUpResult)
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }

    override suspend fun oneTapSignUpWithGoogle(): OneTapSignInUpResponse {
        return try {
            val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
            Success(signUpResult)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    override suspend fun firebaseSignInWithGoogle(
        googleCredential: AuthCredential
    ): SignInUpWithGoogleResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                addUserToFirestore()
            }
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    private suspend fun addUserToFirestore() {
        auth.currentUser?.apply {
            val displayName = displayName ?: throw Exception("Display name is null")
            val email = email ?: throw Exception("Email is null")
            val photoUrl = photoUrl ?: throw Exception("Photo url is null")
            val user = User(
                userId = uid,
                displayName = displayName,
                email = email,
                isEmailVerified = true,
                profilePictureUrl = photoUrl.toString(),
                provider = PROVIDER.GOOGLE
            )
            storageService.saveUser(user)
        }
    }
}
