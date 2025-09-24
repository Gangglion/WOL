package com.glion.wol.data.api.datasource

import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.data.RequestWolStart
import com.glion.wol.data.api.data.ResponseExchangeKey
import com.glion.wol.data.api.data.ResponseToken
import com.glion.wol.data.api.data.ResponseWolStart

/**
 * Project : WOL
 * File : ApiDataSource
 * Created by glion on 2025-09-22
 *
 * Description:
 * - DataSource 정의
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface ApiDataSource {
    /**
     * RSA publicKey -> AESKey 교환
     */
    suspend fun exchangeKey(body: RequestExchangeKey) : ResponseExchangeKey

    /**
     * 토큰 얻기
     */
    suspend fun getToken() : ResponseToken

    /**
     * 토큰 리프레시
     */
    suspend fun refreshToken() : ResponseToken

    /**
     * 기기 전원 켜기
     */
    suspend fun startDevice(body: RequestWolStart) : ResponseWolStart
}