package espressodev.smile.data.service.impl

import com.google.firebase.auth.FirebaseAuth
import espressodev.smile.data.service.AccountService
import espressodev.smile.data.service.ReloadUserResponse
import espressodev.smile.data.service.RevokeAccessResponse
import espressodev.smile.data.service.SendEmailVerificationResponse
import espressodev.smile.data.service.SendPasswordResetEmailResponse
import espressodev.smile.data.service.SignInResponse
import espressodev.smile.data.service.SignUpResponse
import espressodev.smile.data.service.UpdatePasswordResponse
import espressodev.smile.data.service.model.Response
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
}