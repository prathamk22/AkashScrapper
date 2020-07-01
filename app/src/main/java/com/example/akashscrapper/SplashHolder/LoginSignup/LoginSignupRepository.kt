package com.example.akashscrapper.SplashHolder.LoginSignup

import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.network.safeApiCall

class LoginSignupRepository {
    suspend fun login(email: String, password: String) =
        safeApiCall { AkashOnlineLib.api.login(email, password) }
}
