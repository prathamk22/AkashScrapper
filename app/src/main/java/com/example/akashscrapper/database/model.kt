package com.example.akashscrapper.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Login(
    @PrimaryKey
    val id: Int,
    val token: String
)