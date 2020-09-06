package com.example.akashscrapper.network

import com.example.akashscrapper.network.models.AccessToken
import com.example.akashscrapper.network.models.Subjects
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AkashAPICalls {

    @GET("token")
    suspend fun getToken(): Response<AccessToken>

    @GET("api/getSubjects/branchId/{branchId}")
    suspend fun getSubjects(
        @Path("branchId") branchId: Int
    ): Response<List<Subjects>>

}