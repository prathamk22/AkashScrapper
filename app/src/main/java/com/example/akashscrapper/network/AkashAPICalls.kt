package com.example.akashscrapper.network

import com.example.akashscrapper.network.models.AccessToken
import retrofit2.Response
import retrofit2.http.GET

interface AkashAPICalls {

    @GET("token")
    suspend fun getToken(): Response<AccessToken>

}