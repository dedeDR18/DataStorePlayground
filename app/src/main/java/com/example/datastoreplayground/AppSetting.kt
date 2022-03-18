package com.example.datastoreplayground

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class AppSetting(val context: Context){

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("appSetting")

    companion object {
        val LANGUAGE = stringPreferencesKey("LANGUAGE")
        val NOTIFIACATION = booleanPreferencesKey("NOTIFICATION")
    }

    suspend fun storeLanguageSetting(id: String){
        context.dataStore.edit {
            it[LANGUAGE] = id
        }
    }

    suspend fun storeNotificationSetting(boolean: Boolean){
        context.dataStore.edit {
            it[NOTIFIACATION] = boolean
        }
    }

    fun getLanguageSetting() = context.dataStore.data.map {
        it[LANGUAGE] ?: "EN"
    }

    fun getNotificationSetting() = context.dataStore.data.map {
        it[NOTIFIACATION] ?: false
    }
}