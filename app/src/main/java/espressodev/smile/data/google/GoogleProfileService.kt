package espressodev.smile.data.google

import espressodev.smile.data.service.model.GoogleResponse

typealias SignOutResponse = GoogleResponse<Boolean>
typealias RevokeAccessResponse = GoogleResponse<Boolean>

interface GoogleProfileService {
    val displayName: String
    val photoUrl: String

    suspend fun signOut(): SignOutResponse

    suspend fun revokeAccess(): RevokeAccessResponse
}