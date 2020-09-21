package com.example.data.networking

class ErrorStatus {
    companion object {
        const val NO_CONNECTION = "Not Connected To Internet"
        const val UNAUTHORIZED = "You are Unauthorized to View This Page"
        const val NOT_DEFINED = "Please Report Bug"
        const val TIMEOUT = "Request has been Timed out"
        const val EMPTY_RESPONSE = "no data available in repository"
    }
}