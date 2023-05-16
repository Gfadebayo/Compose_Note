package com.exzell.composenote.data

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.exzell.composenote.core.di.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
        @ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_DISPLAY_MODE = intPreferencesKey("display_mode")
    }

    private val Context.datastore by preferencesDataStore(name = "app_pref")

    private val scope = CoroutineScope(Dispatchers.IO)

    fun getDisplayMode(): Flow<Int> {
        return context.datastore.data.map {
            it[KEY_DISPLAY_MODE] ?: Constants.DISPLAY_MODE_GRID
        }
    }

    fun setDisplayMode(mode: Int) {
        context.edit {
            it[KEY_DISPLAY_MODE] = mode
        }
    }

    private fun Context.edit(update: suspend (MutablePreferences) -> Unit) {
        scope.launch {
            datastore.edit(update)
        }
    }
}