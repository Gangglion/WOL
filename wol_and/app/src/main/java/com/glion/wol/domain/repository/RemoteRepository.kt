package com.glion.wol.domain.repository

import com.glion.wol.domain.model.remote.CommonResult
import kotlinx.coroutines.flow.Flow

/**
 * Project : WOL
 * File : RemoteRepository
 * Created by glion on 2025-09-22
 *
 * Description:
 * - API Repository
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface RemoteRepository {
    /**
     * 키 교환 (RSAPublicKey -> AESKey)
     * @param rsaPublicKey RSA Public Key
     * @return 암호화된 AESKey
     */
    suspend fun exchangeKey(rsaPublicKey: ByteArray) : Flow<ByteArray>

    suspend fun getToken() : Flow<String>

    suspend fun refreshToken() : Flow<String>

    suspend fun startDevice(mac: String, iv: String) : Flow<CommonResult>
}