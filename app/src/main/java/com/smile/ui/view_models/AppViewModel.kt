package com.smile.ui.view_models

import androidx.lifecycle.viewModelScope
import com.smile.SmileViewModel
import com.smile.model.service.AuthService
import com.smile.model.service.module.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppViewModel @Inject constructor(
    private val authService: AuthService
) : SmileViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        TODO("IF USER CREATE ACCOUNT SEND THEM TO THE VERIFY ACCOUNT SCREEN CREATE BG THREAD RELOAD USER EVERY 500ms")
        authService.signOut()
        viewModelScope.launch {
            authService.currentUser.collect {
                async { authService.reloadFirebaseUser() }.await()
                when {
                    it == null -> _authState.value = AuthState.Unauthenticated
                    it.isEmailVerified -> _authState.value = AuthState.Authenticated
                    else -> _authState.value = AuthState.Unverified
                }
            }
        }
    }
}