package espressodev.smile.data.google.impl

import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import espressodev.smile.data.google.GoogleProfileService
import espressodev.smile.data.google.RevokeAccessResponse
import espressodev.smile.data.google.SignOutResponse
import espressodev.smile.data.service.StorageService
import espressodev.smile.data.service.module.GoogleResponse.Failure
import espressodev.smile.data.service.module.GoogleResponse.Success
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var signInClient: GoogleSignInClient,
    private val storageService: StorageService,
) : GoogleProfileService {
    override val displayName = auth.currentUser?.displayName.toString()
    override val photoUrl = auth.currentUser?.photoUrl.toString()

    override suspend fun signOut(): SignOutResponse {
        return try {
            oneTapClient.signOut().await()
            auth.signOut()
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    override suspend fun revokeAccess(): RevokeAccessResponse {
        return try {
            auth.currentUser?.apply {
                storageService.deleteUser()
                signInClient.revokeAccess().await()
                oneTapClient.signOut().await()
                delete().await()
            }
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }
    }
}