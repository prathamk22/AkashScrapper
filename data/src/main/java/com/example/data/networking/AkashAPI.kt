package com.example.data.networking

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
        private const val PROD = "api.noteshub.co.in/public"
        private const val APP_ID =
            "540a0a29f96e4d9262467450815ef24a087f96ca019a39cc689eb86314949a15"
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
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader(
                        "x-requested-with",
                        if (communicator.authJwt.isEmpty()) {
                            APP_ID
                        } else {
                            communicator.authJwt
                        }
                    ).build()
            )
        }
        .build()

    private val gson: Gson
        get() = GsonBuilder()
            .setLenient()
            .create()

    private val retrofit = Retrofit.Builder()
        .client(clientInterceptor)
        .baseUrl("https://$PROD/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val api: AkashAPICalls = retrofit.create(
        AkashAPICalls::class.java
    )
}