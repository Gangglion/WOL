package com.glion.api.fcm.data

import kotlinx.serialization.Serializable

@Serializable
data class RequestSendPush(
    val mac: String,
    val status: String
)
