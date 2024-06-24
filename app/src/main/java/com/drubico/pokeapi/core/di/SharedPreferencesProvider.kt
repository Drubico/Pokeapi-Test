package com.drubico.pokeapi.core.di

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

    fun getIntegerValue(preference: PREFERENCES): Int {
        return sharedPreferences.getInt(preference.name, -1)
    }

    fun clearValue(preference: PREFERENCES) {
        sharedPreferences.edit().remove(preference.name).apply()
    }

    fun clearAllValues() {
        sharedPreferences.edit().clear().apply()
    }

    fun getBool(preference: PREFERENCES): Boolean {
        return sharedPreferences.getBoolean(preference.name, false)
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

enum class PREFERENCES {
    PAGE,
    LIMIT,
}