package com.glion.wol.data.api.data

/**
 * Project : WOL
 * File : RequestExchangeKey
 * Created by glion on 2025-09-22
 *
 * Description:
 * - 키 교환 Request
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class RequestExchangeKey(
    val base64EncodedRsaPublicKey: String
)
