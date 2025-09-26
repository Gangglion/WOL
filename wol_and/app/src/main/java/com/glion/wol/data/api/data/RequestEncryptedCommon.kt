package com.glion.wol.data.api.data

/**
 * Project : WOL
 * File : RequestWolStart
 * Created by glion on 2025-09-22
 *
 * Description:
 * - 암호화된 데이터 API Request
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class RequestEncryptedCommon(
    val encryptedData: String,
    val iv: String
)
