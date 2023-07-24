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
        authService.signOut()
        viewModelScope.launch {
            authService.currentUser.collect {
                async { authService.reloadFirebaseUser() }.await()
                when (it) {
                    null -> _authState.value = AuthState.Unauthenticated
                    else -> _authState.value = AuthState.Unverified
                }
            }
        }
    }
}