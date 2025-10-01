package com.glion.wol.domain.repository

import com.glion.wol.domain.model.remote.CommonResult

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
    suspend fun exchangeKey(rsaPublicKey: ByteArray) : ByteArray

    /**
     * AccessToken 얻기
     * @return AccessToken
     */
    suspend fun getToken() : String

    /**
     * AccessToken 갱신
     * @return 새로운 AccessToken
     */
    suspend fun refreshToken() : String

    /**
     * 기기 전원 켜기
     * @param mac 전원을 켤 맥 주소
     * @return 결과 공통 객체
     */
    suspend fun startDevice(mac: String) : CommonResult

    /**
     * FCM 토큰 서버로 전송
     * @param fcmToken FCM Token 문자열
     * @return 결과와 message 가 있는 일반적인 Return Type
     */
    suspend fun sendFcmToken(fcmToken: String) : CommonResult
}