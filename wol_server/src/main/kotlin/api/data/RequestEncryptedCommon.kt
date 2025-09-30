package com.glion.api.data

import kotlinx.serialization.Serializable

@Serializable
data class RequestEncryptedCommon(
    val encryptedDataBase64: String,
    val ivBase64: String
)
