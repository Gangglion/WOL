package com.glion.wol.data.api.data

/**
 * Project : WOL
 * File : RequestWolStart
 * Created by glion on 2025-09-22
 *
 * Description:
 * - 암호화된 데이터 API Request
 * @param encryptedDataBase64 Encrypted Data encoded in Base64
 * @param ivBase64 iv values encoded in Base64
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class RequestEncryptedCommon(
    val encryptedDataBase64: String,
    val ivBase64: String
)
