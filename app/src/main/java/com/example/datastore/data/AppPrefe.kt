package com.example.datastore.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

object AppPrefe {
    val Context.dataStore by preferencesDataStore(name = "setting")


}