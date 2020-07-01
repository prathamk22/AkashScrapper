package com.example.akashscrapper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akashscrapper.SplashHolder.SplashScreenFrag
import com.example.akashscrapper.utils.replaceFragmentSafely


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragmentSafely(
            SplashScreenFrag(),
            "SplashScreen",
            containerViewId = R.id.frameLayout,
            addToStack = false
        )
    }
}