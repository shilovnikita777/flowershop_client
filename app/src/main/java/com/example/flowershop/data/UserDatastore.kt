package com.example.flowershop.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.flowershop.util.Constants.NO_USER_CONSTANT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDatastore(
    private val context: Context
) {
    private val Context.datastore : DataStore<Preferences> by preferencesDataStore("user_info")

    val getUserId : Flow<Int> = context.datastore.data
        .map { preferences ->
            preferences[USER_ID_KEY] ?: NO_USER_CONSTANT
        }

    suspend fun saveUserId(userId: Int) {
        context.datastore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun deleteUserId() {
        context.datastore.edit {
            it.remove(USER_ID_KEY)
        }
    }

    companion object {
        val USER_ID_KEY = intPreferencesKey("user_id")
    }
}