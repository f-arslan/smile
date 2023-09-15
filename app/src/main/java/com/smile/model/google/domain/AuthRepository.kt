package com.smile.model.google.domain

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.smile.model.service.module.GoogleResponse

typealias OneTapSignInResponse = GoogleResponse<BeginSignInResult>
typealias SignInWithGoogleResponse = GoogleResponse<Boolean>

interface AuthRepository {
    val isUserAuthenticatedInFirebase: Boolean

    suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse

    suspend fun oneTapSignUpWithGoogle(): OneTapSignInResponse

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse
}