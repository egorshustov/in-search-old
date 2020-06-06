package com.egorshustov.vpoiske.util

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class DelegatedPreference<T>(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defValue: T
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(defValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findPreference(defaultValue: T): T = with(sharedPreferences) {
        val foundValue: Any = when (defaultValue) {
            is String -> getString(key, defaultValue)
            is Int -> getInt(key, defaultValue)
            is Long -> getLong(key, defaultValue)
            is Float -> getFloat(key, defaultValue)
            is Boolean -> getBoolean(key, defaultValue)
            else -> throw IllegalArgumentException("This type cannot be obtained from Preferences")
        }
        foundValue as T
    }

    private fun putPreference(value: T?) = with(sharedPreferences.edit()) {
        value ?: return@with
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            else -> throw IllegalArgumentException("This type cannot be saved into Preferences")
        }.apply()
    }
}