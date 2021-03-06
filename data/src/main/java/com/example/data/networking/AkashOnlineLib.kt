package com.example.data.networking

object AkashOnlineLib {
    private var clients: AkashAPI? = null

    fun initialize(communicator: APICommunicator) {
        clients = AkashAPI(communicator)
    }

    private fun getClient() = checkNotNull(clients, { "CBOnlineLib.initialize() not called" })

    val api get() = getClient().api

    var httpLogging
        get() = getClient().getHttpLogging()
        set(value) = getClient()
            .setHttpLogging(value)
}