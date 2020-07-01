package com.example.akashscrapper.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AkashAPI internal constructor(
    private val communicator: APICommunicator
) {
    companion object {
        private const val PROD = "akash-api.herokuapp.com/"
        const val CONNECT_TIMEOUT = 15
        const val READ_TIMEOUT = 15
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
    }

    fun setHttpLogging(enabled: Boolean) {
        logging.level =
            if (enabled)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
    }

    fun getHttpLogging(): Boolean = when (logging.level) {
        HttpLoggingInterceptor.Level.BODY -> true
        else -> false
    }

    private val clientInterceptor = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
        .addInterceptor(logging)
        .addInterceptor { chain ->
            if (communicator.authJwt.isEmpty())
                chain.proceed(chain.request())
            else chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${communicator.authJwt}").build()
            )
        }
        .build()

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .client(clientInterceptor)
        .baseUrl("http://$PROD/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val api: AkashAPICalls = retrofit.create(
        AkashAPICalls::class.java
    )
}