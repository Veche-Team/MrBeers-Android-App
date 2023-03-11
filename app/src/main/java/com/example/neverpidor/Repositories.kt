package com.example.neverpidor

import android.content.Context
import com.example.neverpidor.model.settings.AppSettings
import com.example.neverpidor.model.settings.SharedPreferencesAppSettings

object Repositories {

    private lateinit var appContext: Context

    val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(appContext)
    }

    fun init(context: Context) {
        appContext = context
    }
}