package com.smile.model.google

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.smile.model.google.data.AuthRepositoryImpl
import com.smile.model.google.data.ProfileRepositoryImpl
import com.smile.model.google.domain.AuthRepository
import com.smile.model.google.domain.ProfileRepository
import com.smile.model.service.StorageService
import com.smile.util.Constants.SIGN_IN_REQUEST
import com.smile.util.Constants.SIGN_UP_REQUEST
import com.smile.util.Secrets.WEB_CLIENT_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class GoogleModule {
    @Provides
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ) = Identity.getSignInClient(context)

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun provideSignInRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(WEB_CLIENT_ID)
                .setFilterByAuthorizedAccounts(true)
                .build())
        .setAutoSelectEnabled(true)
        .build()

    @Provides
    @Named(SIGN_UP_REQUEST)
    fun provideSignUpRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(WEB_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build())
        .build()

    @Provides
    fun provideGoogleSignInOptions() = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(WEB_CLIENT_ID)
        .requestEmail()
        .build()

    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest,
        storageService: StorageService,
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signUpRequest = signUpRequest,
        storageService = storageService
    )

    @Provides
    fun provideProfileRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        signInClient: GoogleSignInClient,
        storageService: StorageService
    ): ProfileRepository = ProfileRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInClient = signInClient,
        storageService = storageService
    )
}