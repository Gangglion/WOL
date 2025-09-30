package com.glion.api.data

import kotlinx.serialization.Serializable

@Serializable
data class RespondCommon(
    val result: Boolean,
    val message: String = ""
)