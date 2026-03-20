package com.example.max.data.database.preferences


import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val KEY_MY_ID = stringPreferencesKey("my_personal_id")

@Singleton
class UserPrefs @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = "user_settings")

    suspend fun saveMyId(id: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_MY_ID] = id
        }
    }

    fun getMyID(): Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_MY_ID] ?: "default"
    }
}