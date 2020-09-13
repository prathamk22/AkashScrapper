package com.example.akashscrapper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import com.example.akashscrapper.dashboard.DashboardActivity

@ExperimentalPagingApi
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(applicationContext, DashboardActivity::class.java))
        finish()
    }
}