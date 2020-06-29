package com.example.akashscrapper.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.akashscrapper.R
import kotlinx.android.synthetic.main.activity_main.*


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val anim: Animation = ScaleAnimation(
            0f, 1f,  // Start and end values for the X axis scaling
            0f, 1f,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 1f
        )
        anim.fillAfter = true
        anim.duration = 300

        object : CountDownTimer(3000, 3000) {
            override fun onFinish() {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }

            override fun onTick(p0: Long) {

            }
        }.start()
        lottie.startAnimation(anim)
    }
}