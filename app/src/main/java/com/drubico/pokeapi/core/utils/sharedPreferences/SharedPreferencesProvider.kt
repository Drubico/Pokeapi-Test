package com.drubico.pokeapi.core.utils.sharedPreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SharedPreferencesProvider
@Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun getValue(preference: PREFERENCES): String? {
        return sharedPreferences.getString(preference.name, null)
    }

    fun setValue(preference: PREFERENCES, value: String) {
        sharedPreferences.apply {
            edit {
                putString(preference.name, value)
            }
        }
    }

    fun setIntegerValue(preference: PREFERENCES, value: Int) {
        sharedPreferences.apply {
            edit {
                putInt(preference.name, value)
            }
        }
    }

    fun getIntegerValue(preference: PREFERENCES, default: Int = -1): Int {
        return sharedPreferences.getInt(preference.name, default)
    }

    fun clearValue(preference: PREFERENCES) {
        sharedPreferences.edit().remove(preference.name).apply()
    }

    fun clearAllValues() {
        sharedPreferences.edit().clear().apply()
    }

    fun getBool(preference: PREFERENCES, default: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(preference.name, default)
    }

    fun setBool(preference: PREFERENCES, value: Boolean) {
        sharedPreferences.apply {
            edit {
                putBoolean(preference.name, value)
            }
        }
    }

    companion object {
        const val CONTAINER_NAME = "appendix"
    }
}
