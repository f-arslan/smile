package com.smile.model.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val userDataStorePreferences: DataStore<Preferences>
) {
}