package com.example.akashscrapper.utils

import android.content.SharedPreferences

fun SharedPreferences.save(key: String, value: Any) {
    with(this.edit()) {
        when (value) {
            is Float -> putFloat(key, value).apply()
            is Boolean -> putBoolean(key, value).apply()
            is Int -> putInt(key, value).apply()
            is String -> putString(key, value).apply()
            else -> UnsupportedOperationException("Unsupported Type to save")
        }
    }
}
