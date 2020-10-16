package com.example.core

import com.example.data.networking.APICommunicator
import com.example.data.networking.AkashOnlineLib
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class APITests {

    //Need to update the token if this expires.
    var token =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJOakV5WTJVM09ETmhaVEZrWm1JNU5EUmlORFppWXpCaE1UZ3lOakppTUdSa01HRXdZMkU0TURNd05tUmxaRGRsTURGa1lqSTROVEkxTXpFd01XVTFOUT09IiwiaXNzIjoiaHR0cHM6XC9cL2FwaS5ub3Rlc2h1Yi5jby5pbiIsInN1YiI6IkFDQ0VTUy1UTy1OT1RFU0hVQi1SRVNULUFQSSIsImF1ZCI6Imh0dHBzOlwvXC9ub3Rlc2h1Yi5jby5pbiIsImlhdCI6MTYwMjg2NDE4MX0.xhX9yuCtawviqc9WhUlUYGjpky-J9fCPjsSv85nSIlc"

    @Before
    fun initialize() {
        AkashOnlineLib.initialize(object : APICommunicator {
            override var authJwt: String
                get() = token
                set(value) {}
            override var refreshToken: String
                get() = token
                set(value) {}

        })
    }

    //Call to update the token so that Other calls can execute properly
    @Test
    fun `GET ACCESS TOKEN`() {
        val response = runBlocking { AkashOnlineLib.api.getToken() }
        assertTrue(response.isSuccessful)
        assertNotNull(response.body()?.accessToken)
    }

    @Test
    fun `GET COURSE`() {
        val response = runBlocking { AkashOnlineLib.api.getSubjects(1) }
        assertTrue(response.code() == 200)
        assertNotNull(response.body())
    }

    @Test
    fun `GET SUBJECTS`() {
        val response =
            runBlocking { AkashOnlineLib.api.getFilesBySubject("Applied Mathematics 1", 1) }
        assertTrue(response.code() == 200)
        assertNotNull(response.body())
    }
}

