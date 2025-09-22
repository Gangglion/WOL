package com.glion.wol.domain.repository

import com.glion.wol.domain.model.remote.WolResult
import com.glion.wol.util.FlowResult
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
     */
    suspend fun exchangeKey(rsaPublicKey: ByteArray) : Flow<FlowResult<ByteArray>>

    suspend fun getJwtToken() : Flow<FlowResult<String>>

    suspend fun refreshJwtToken() : Flow<FlowResult<String>>

    suspend fun startDevice(mac: String, iv: String) : Flow<FlowResult<WolResult>>
}