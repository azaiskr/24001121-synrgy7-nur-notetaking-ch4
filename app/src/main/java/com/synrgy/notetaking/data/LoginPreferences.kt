package com.synrgy.notetaking.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.synrgy.notetaking.data.database.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "loginSession")

class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveLoginPref(user: User) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
            preferences[PASSWORD_KEY] = user.password
        }
    }

    fun getLoginPref(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[USERNAME_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: ""
            )
        }
    }

    suspend fun clearLoginPref() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null

        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")

        fun getInstance(context: Context): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(context.dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}