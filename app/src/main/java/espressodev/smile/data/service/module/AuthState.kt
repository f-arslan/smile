package espressodev.smile.data.service.module

import espressodev.smile.data.User

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Unauthenticated : AuthState()
}