package com.glion.wol.data.api.data

/**
 * Project : WOL
 * File : RequestWolStart
 * Created by glion on 2025-09-22
 *
 * Description:
 * - WOL API Request
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class RequestWolStart(
    val mac: String,
    val iv: String
)
