package com.smile.model.google.data

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.smile.model.User
import com.smile.model.google.domain.AuthRepository
import com.smile.model.google.domain.OneTapSignInResponse
import com.smile.model.google.domain.SignInWithGoogleResponse
import com.smile.model.service.StorageService
import com.smile.model.service.module.GoogleResponse.Failure
import com.smile.model.service.module.GoogleResponse.Success
import com.smile.util.Constants.SIGN_IN_REQUEST
import com.smile.util.Constants.SIGN_UP_REQUEST
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val storageService: StorageService,
) : AuthRepository {
    override val isUserAuthenticatedInFirebase = auth.currentUser != null

    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
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

    override suspend fun oneTapSignUpWithGoogle(): OneTapSignInResponse {
        return try {
            val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
            Success(signUpResult)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    override suspend fun firebaseSignInWithGoogle(
        googleCredential: AuthCredential
    ): SignInWithGoogleResponse {
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
                profilePictureUrl = photoUrl.toString()
            )
            storageService.saveUser(user)
        }
    }
}
