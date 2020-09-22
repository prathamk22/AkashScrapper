package com.example.core

import android.app.Application
import com.example.core.di.databaseModule
import com.example.core.di.preferencesModule
import com.example.core.di.viewModelModule
import com.example.core.utils.PreferenceHelper
import com.example.data.networking.APICommunicator
import com.example.data.networking.AkashOnlineLib
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class CoreApp : Application() {

    companion object{
        lateinit var mInstance: CoreApp
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        val prefs = PreferenceHelper.getPrefs(this)

        AkashOnlineLib.initialize(object :
            APICommunicator {

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

        if (BuildConfig.DEBUG){
            AkashOnlineLib.httpLogging = true
        }

        startKoin {
            androidContext(this@CoreApp)
            modules(listOf(viewModelModule, databaseModule, preferencesModule))
        }
    }
}