package com.glion.wol.data.api.data

/**
 * Project : WOL
 * File : RequestJwtToken
 * Created by glion on 2025-09-22
 *
 * Description:
 * - JWT 토큰 요청 Request
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class RequestJwtToken(
    val appKey: String,
    val iv: String
)
