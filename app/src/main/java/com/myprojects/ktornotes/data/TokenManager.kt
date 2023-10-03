package com.myprojects.ktornotes.data

import android.content.SharedPreferences
import com.myprojects.ktornotes.util.Constants

class TokenManager(
    private val preferences: SharedPreferences
) {
    val accessToken get() = preferences.getString(Constants.PREFS_JWT_KEY, null)

    fun saveToken(token: String) {
        preferences.edit()
            .putString(Constants.PREFS_JWT_KEY, token)
            .apply()
    }

    fun deleteToken() {
        preferences.edit()
            .remove(Constants.PREFS_JWT_KEY)
            .apply()
    }
}