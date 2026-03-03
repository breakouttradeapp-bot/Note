package com.smartnotes.notepadplusplus.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class PreferencesManager(private val context: Context) {

    companion object {
        val KEY_DARK_MODE        = booleanPreferencesKey("dark_mode")
        val KEY_SORT_BY          = stringPreferencesKey("sort_by")
        val KEY_VIEW_MODE        = stringPreferencesKey("view_mode")        // "list" | "grid"
        val KEY_APP_LOCK_ENABLED = booleanPreferencesKey("app_lock_enabled")
        val KEY_APP_PIN          = stringPreferencesKey("app_pin")
        val KEY_THEME_COLOR      = stringPreferencesKey("theme_color")
        val KEY_NOTE_LOCK_PIN    = stringPreferencesKey("note_lock_pin")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_DARK_MODE] ?: false }

    val sortBy: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_SORT_BY] ?: "updated" }

    val viewMode: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_VIEW_MODE] ?: "list" }

    val isAppLockEnabled: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_APP_LOCK_ENABLED] ?: false }

    val noteLockPin: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_NOTE_LOCK_PIN] ?: "" }

    suspend fun setDarkMode(value: Boolean) {
        context.dataStore.edit { it[KEY_DARK_MODE] = value }
    }

    suspend fun setSortBy(value: String) {
        context.dataStore.edit { it[KEY_SORT_BY] = value }
    }

    suspend fun setViewMode(value: String) {
        context.dataStore.edit { it[KEY_VIEW_MODE] = value }
    }

    suspend fun setAppLockEnabled(value: Boolean) {
        context.dataStore.edit { it[KEY_APP_LOCK_ENABLED] = value }
    }

    suspend fun setNoteLockPin(pin: String) {
        context.dataStore.edit { it[KEY_NOTE_LOCK_PIN] = pin }
    }
}
