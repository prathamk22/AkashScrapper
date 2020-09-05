package com.example.akashscrapper.dashboard

import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.network.safeApiCall

class DashboardRepository {

    suspend fun getToken() = safeApiCall { AkashOnlineLib.api.getToken() }

}