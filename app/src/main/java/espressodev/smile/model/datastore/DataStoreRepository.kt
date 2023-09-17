package espressodev.smile.model.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val userDataStorePreferences: DataStore<Preferences>
) {

    suspend fun setNotificationsEnabled(notificationState: String) {
        userDataStorePreferences.edit { preferences ->
            preferences[NOTIFICATION_SHOW_STATE] = notificationState
        }
    }

    suspend fun setOnboardingScreenState(onboardingScreenState: Boolean) {
        userDataStorePreferences.edit { preferences ->
            preferences[ONBOARDING_SCREEN_STATE] = onboardingScreenState
        }
    }

    suspend fun setFcmToken(fcmToken: String) {
        userDataStorePreferences.edit { preferences ->
            preferences[FCM_TOKEN] = fcmToken
        }
    }

    suspend fun clearAllInformation() {
        userDataStorePreferences.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun getFcmToken(): String {
        val preferences = userDataStorePreferences.data.first()
        return preferences[FCM_TOKEN] ?: ""
    }

    suspend fun getOnboardingScreenState(): Boolean {
        val preferences = userDataStorePreferences.data.first()
        return preferences[ONBOARDING_SCREEN_STATE] ?: false
    }

    suspend fun getNotificationsEnabled(): String {
        val preferences = userDataStorePreferences.data.first()
        return preferences[NOTIFICATION_SHOW_STATE] ?: IDLE
    }

    companion object {
        val NOTIFICATION_SHOW_STATE = stringPreferencesKey("notification_show_state")
        val ONBOARDING_SCREEN_STATE = booleanPreferencesKey("onboarding_screen_state")
        val FCM_TOKEN = stringPreferencesKey("fcm_token")
        const val IDLE = "IDLE"
        const val ENABLED = "ENABLED"
        const val DISABLED = "DISABLED"
    }
}