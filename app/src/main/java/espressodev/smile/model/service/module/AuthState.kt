package espressodev.smile.model.service.module

import espressodev.smile.model.User

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Unauthenticated : AuthState()
}