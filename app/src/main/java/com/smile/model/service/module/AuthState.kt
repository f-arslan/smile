package com.smile.model.service.module

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    object Unverified : AuthState()
    object Authenticated : AuthState()
}