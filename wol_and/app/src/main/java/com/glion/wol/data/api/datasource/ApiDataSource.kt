package com.glion.wol.data.api.datasource

import com.glion.wol.data.api.data.RequestEncryptedCommon
import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.data.ResponseCommon
import com.glion.wol.data.api.data.ResponseEncryptedCommon

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
    suspend fun exchangeKey(body: RequestExchangeKey) : ResponseEncryptedCommon

    /**
     * 토큰 얻기
     */
    suspend fun getToken(body: RequestEncryptedCommon) : ResponseEncryptedCommon

    /**
     * 푸시 토큰 전송
     */
    suspend fun sendPushToken(body: RequestEncryptedCommon) : ResponseCommon

    /**
     * 기기 전원 켜기
     */
    suspend fun startDevice(body: RequestEncryptedCommon) : ResponseCommon
}