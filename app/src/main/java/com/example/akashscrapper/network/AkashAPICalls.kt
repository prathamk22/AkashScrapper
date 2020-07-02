package com.example.akashscrapper.network

import com.example.akashscrapper.network.models.Login
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AkashAPICalls {

    @POST("login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<Login>

    @POST("signup")
    suspend fun signUp(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<Login>

}