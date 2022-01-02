package com.example.core

import com.example.data.networking.APICommunicator
import com.example.data.networking.AkashOnlineLib
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AccessTokenTest {

    @Before
    fun initialize() {
        AkashOnlineLib.initialize(object : APICommunicator {
            override var authJwt: String
                get() = ""
                set(value) {}
            override var refreshToken: String
                get() = ""
                set(value) {}

        })
    }

    //Call to update the token so that Other calls can execute properly
    @Test
    fun `GET ACCESS TOKEN`() {
        val response = runBlocking { AkashOnlineLib.api.getToken() }
        Assert.assertTrue(response.isSuccessful)
        Assert.assertNotNull(response.body()?.accessToken)
    }

}
