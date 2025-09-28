package com.glion.wol.data.api.datasource

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
    suspend fun exchangeKey(rsaPublicKey: ByteArray) : ResponseEncryptedCommon

    /**
     * 토큰 얻기
     */
    suspend fun getToken() : ResponseEncryptedCommon

    /**
     * 토큰 리프레시
     */
    suspend fun refreshToken() : ResponseEncryptedCommon

    /**
     * 푸시 토큰 전송
     */
    suspend fun sendPushToken(fcmToken: String) : ResponseCommon

    /**
     * 기기 전원 켜기
     */
    suspend fun startDevice(mac: String) : ResponseCommon
}