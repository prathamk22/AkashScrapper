package com.example.akashscrapper.network.models

open class BaseModel {
    val message: String = ""
}

data class Login(
    val token: String,
    val name: String,
    val collegeCourse: String,
    val branch: String
) : BaseModel()