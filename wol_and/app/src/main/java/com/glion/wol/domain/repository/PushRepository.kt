package com.glion.wol.domain.repository

import com.glion.wol.domain.model.remote.CommonResult
import kotlinx.coroutines.flow.Flow

/**
 * Project : WOL
 * File : PushRepository
 * Created by glion on 2025-10-13
 *
 * Description:
 * - FCM 관련 Repository
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface PushRepository {
    /**
     * 저장된 FCM Token 가져오기
     */
    fun getFcmToken() : Flow<String?>

    /**
     * FCM Token DataStore 에 저장
     * @param fcmToken FCM Token
     */
    suspend fun saveFcmToken(fcmToken: String)

    /**
     * FCM 토큰 서버로 전송
     * @param fcmToken FCM Token 문자열
     * @return 결과와 message 가 있는 일반적인 Return Type
     */
    suspend fun sendFcmToken(fcmToken: String) : CommonResult

    /**
     * Push Data 복호화
     */
    suspend fun decryptedPushData(encryptedData: ByteArray, iv: ByteArray) : String
}