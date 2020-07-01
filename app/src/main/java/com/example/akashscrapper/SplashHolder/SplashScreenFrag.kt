package com.example.akashscrapper.SplashHolder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.fragment.app.Fragment
import com.example.akashscrapper.R
import com.example.akashscrapper.SplashHolder.LoginSignup.LoginFragment
import com.example.akashscrapper.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.fragment_splash_screen.*

class SplashScreenFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anim: Animation = ScaleAnimation(
            0f, 1f,
            0f, 1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f
        )
        anim.fillAfter = true
        anim.duration = 300
        lottie.startAnimation(anim)

        login.setOnClickListener {
            replaceFragmentSafely(
                LoginFragment(),
                "LoginFragment",
                containerViewId = R.id.frameLayout,
                addToStack = true
            )
        }

        signUp.setOnClickListener {

        }
    }
}