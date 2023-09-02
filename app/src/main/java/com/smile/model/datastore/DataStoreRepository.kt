package com.smile.model.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

    suspend fun getNotificationsEnabled(): String {
        val preferences = userDataStorePreferences.data.first()
        return preferences[NOTIFICATION_SHOW_STATE] ?: IDLE
    }

    companion object {
        val NOTIFICATION_SHOW_STATE = stringPreferencesKey("notification_show_state")
        const val IDLE = "IDLE"
        const val ENABLED = "ENABLED"
        const val DISABLED = "DISABLED"
    }
}