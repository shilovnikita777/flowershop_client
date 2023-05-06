package com.example.flowershop.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.flowershop.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(
    private val context: Context
) {
    private val Context.datastore : DataStore<Preferences> by preferencesDataStore("user_token")

    fun getToken() : Flow<String> = context.datastore.data
        .map { preferences ->
            preferences[USER_TOKEN_KEY] ?: ""
        }

    suspend fun saveToken(token: String) {
        context.datastore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }

    suspend fun deleteToken() {
        context.datastore.edit {
            it.remove(USER_TOKEN_KEY)
        }
    }

    companion object {
        val USER_TOKEN_KEY = stringPreferencesKey("user_token")
    }
}