package com.smile.model.google.domain

import com.smile.model.service.module.GoogleResponse

typealias SignOutResponse = GoogleResponse<Boolean>
typealias RevokeAccessResponse = GoogleResponse<Boolean>

interface ProfileRepository {
    val displayName: String
    val photoUrl: String

    suspend fun signOut(): SignOutResponse

    suspend fun revokeAccess(): RevokeAccessResponse
}