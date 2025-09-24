package com.glion.wol.data.api.data

/**
 * Project : WOL
 * File : RequestToken
 * Created by glion on 2025-09-22
 *
 * Description:
 * - Header Authorization 토큰 요청 Request
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class RequestToken(
    val appKey: String,
    val iv: String
)
