package com.example.akashscrapper

import android.app.Application
import com.example.akashscrapper.di.databaseModule
import com.example.akashscrapper.di.preferencesModule
import com.example.akashscrapper.di.viewModelModule
import com.example.akashscrapper.network.APICommunicator
import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.utils.PreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AkashApp : Application() {
    companion object {
        lateinit var mInstance: AkashApp
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        val prefs = PreferenceHelper.getPrefs(this)

        AkashOnlineLib.initialize(object : APICommunicator {

            override var authJwt: String
                get() = prefs.SP_JWT_TOKEN_KEY
                set(value) {
                    prefs.SP_JWT_TOKEN_KEY = value
                }
            override var refreshToken: String
                get() = prefs.SP_JWT_REFRESH_TOKEN
                set(value) {
                    prefs.SP_JWT_REFRESH_TOKEN = value
                }

        })

        if (BuildConfig.DEBUG) {
            AkashOnlineLib.httpLogging = true
        }

        startKoin {
            androidContext(this@AkashApp)
            modules(listOf(viewModelModule, databaseModule, preferencesModule))
        }
    }
}