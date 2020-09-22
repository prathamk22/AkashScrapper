package com.example.akashscrapper

import com.example.core.CoreApp
import com.facebook.stetho.Stetho

class AkashApp : CoreApp() {
    companion object {
        lateinit var mInstance: AkashApp
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}