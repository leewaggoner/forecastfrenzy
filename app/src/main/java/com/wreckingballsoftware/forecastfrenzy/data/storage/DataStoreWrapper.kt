package com.wreckingballsoftware.forecastfrenzy.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class DataStoreWrapper(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKey {
        val PLAYER_ID_KEY = longPreferencesKey("PlayerId")
        val PLAYER_NAME_KEY = stringPreferencesKey("PlayerName")
    }

    suspend fun putPlayerId(id: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.PLAYER_ID_KEY] = id
        }
    }

    suspend fun getPlayerId(default: Long): Long = withContext(Dispatchers.IO) {
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.first()[PreferencesKey.PLAYER_ID_KEY] ?: default
    }

    suspend fun putPlayerName(name: String) = withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.PLAYER_NAME_KEY] = name
        }
    }

    suspend fun getPlayerName(default: String): String = withContext(Dispatchers.IO) {
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.first()[PreferencesKey.PLAYER_NAME_KEY] ?: default
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        dataStore.edit {
            it.clear()
        }
    }
}