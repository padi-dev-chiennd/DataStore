package com.example.datastore.data

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private const val PREFS_NAME = "MyAppPrefs"
    private const val KEY_THEME_MODE = "themeMode"

//    fun setThemeMode(context: Context, isDarkMode: Boolean) {
//        val prefs: SharedPreferences =
//            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        prefs.edit().putBoolean(KEY_THEME_MODE, isDarkMode).apply()
//    }
    fun setThemeMode(context: Context, isDarkMode: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_THEME_MODE, isDarkMode).apply()
    }


    fun getThemeMode(context: Context): Boolean {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_THEME_MODE, false) // Default to false (light mode)
    }

}