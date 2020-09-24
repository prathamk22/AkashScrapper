package com.example.core.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper private constructor() {

    companion object {
        private const val PREFS_FILENAME = "com.example.akashscrapper.prefs"
        const val JWT_TOKEN = "jwt_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val NAME = "name"
        const val SELECTED_COURSE = "selected_course"

        private var prefs: SharedPreferences? = null
        private var instance: PreferenceHelper = PreferenceHelper()

        fun getPrefs(context: Context): PreferenceHelper {
            if (prefs == null) {
                prefs = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
            }
            return instance
        }
    }

    var SP_JWT_TOKEN_KEY: String
        get() = prefs?.getString(JWT_TOKEN, "") ?: ""
        set(value) {
            prefs?.save(JWT_TOKEN, value)
        }

    var NAME: String
        get() = prefs?.getString(NAME, "") ?: ""
        set(value) {
            prefs?.save(NAME, value)
        }

    var SP_SELECTED_COURSE: Int
        get() = prefs?.getInt(SELECTED_COURSE, 0) ?: 0
        set(value) {
            prefs?.save(SELECTED_COURSE, value)
        }


    var SP_JWT_REFRESH_TOKEN: String
        get() = prefs?.getString(REFRESH_TOKEN, "") ?: ""
        set(value) {
            prefs?.save(REFRESH_TOKEN, value)
        }

}