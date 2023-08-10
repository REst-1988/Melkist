package com.example.melkist.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private const val OPTIONS_PREFERENCES = "options_preferences"
private val Context.dataStore by preferencesDataStore(name = OPTIONS_PREFERENCES)

object OptionsDs {
    private var instance: OptionsDataStore? = null
    fun getDataStore(context: Context): OptionsDataStore {
        if (instance == null)
            instance = OptionsDataStore(context)
        return instance!!
    }
}

class OptionsDataStore(context: Context) {
    private val notificationsPk = intPreferencesKey("notifications") // userIdPreferencesKey
    private val themePk = intPreferencesKey("theme")

    suspend fun saveNotificationsOptionToPreferencesStore(
        notifOption: Int, context: Context
    ) {
        context.dataStore.edit { preferences ->
            notifOption.let { option ->
                preferences[notificationsPk] = option
            }
        }
    }

    suspend fun saveThemeOptionToPreferencesStore(
        themeOption: Int, context: Context
    ) {
        context.dataStore.edit { preferences ->
            themeOption.let { option ->
                preferences[themePk] = option
            }
        }
    }

    val notifPreferenceFlow: Flow<Int?> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[notificationsPk]
    }

    val themePreferenceFlow: Flow<Int?> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[themePk]
    }

}