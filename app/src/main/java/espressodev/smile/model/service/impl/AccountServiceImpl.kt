package espressodev.smile.model.service.impl

import com.google.firebase.auth.FirebaseAuth
import espressodev.smile.model.service.AccountService
import espressodev.smile.model.service.AuthStateResponse
import espressodev.smile.model.service.ReloadUserResponse
import espressodev.smile.model.service.RevokeAccessResponse
import espressodev.smile.model.service.SendEmailVerificationResponse
import espressodev.smile.model.service.SendPasswordResetEmailResponse
import espressodev.smile.model.service.SignInResponse
import espressodev.smile.model.service.SignUpResponse
import espressodev.smile.model.service.UpdatePasswordResponse
import espressodev.smile.model.service.module.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val isEmailVerified: Boolean
        get() = auth.currentUser?.isEmailVerified ?: false

    override val email: String
        get() = auth.currentUser?.email.orEmpty()
    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String,
        password: String
    ): SignUpResponse {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendEmailVerification(): SendEmailVerificationResponse {
        return try {
            auth.currentUser?.sendEmailVerification()?.await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun firebaseSignInWithEmailAndPassword(
        email: String,
        password: String
    ): SignInResponse {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun reloadFirebaseUser(): ReloadUserResponse {
        return try {
            auth.currentUser?.reload()?.await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updatePassword(password: String): UpdatePasswordResponse {
        return try {
            auth.currentUser?.updatePassword(password)?.await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun signOut() = auth.signOut()

    override suspend fun revokeAccess(): RevokeAccessResponse {
        return try {
            auth.currentUser?.delete()?.await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            this.trySend(auth.currentUser != null)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

}