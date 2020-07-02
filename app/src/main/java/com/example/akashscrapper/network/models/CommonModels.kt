package com.example.akashscrapper.network.models

open class BaseModel {
    val message: String = ""
}

data class Login(
    val token: String
) : BaseModel()