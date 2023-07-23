package com.smile.model.service.module

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.smile.model.service.AuthService
import com.smile.model.service.impl.AuthServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {
    @Provides
    fun provideAuthService(): AuthService = AuthServiceImpl(
        auth = Firebase.auth
    )
}