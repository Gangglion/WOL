package com.glion.wol.domain.repository

import com.glion.wol.domain.model.remote.CommonResult
import kotlinx.coroutines.flow.Flow

/**
 * Project : WOL
 * File : FcmRepository
 * Created by glion on 2025-09-26
 *
 * Description:
 * - FCM 관련 작업 Repository
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface FcmRepository {
    /**
     * FCM 토큰 서버로 전송
     * @param fcmToken FCM Token 문자열
     * @return 결과와 message 가 있는 일반적인 Return Type
     */
    suspend fun sendFcmToken(fcmToken: String) : Flow<CommonResult>

    /**
     * 기기 전원상태 관리
     * @param macAddr 전원상태를 변경할 맥 주소
     * @param status 전원상태
     * @return Unit
     */
    suspend fun changePowerStatus(macAddr: String, status: Boolean) : Flow<Unit>

    /**
     * 맥 주소에 따른 별칭 가져오기
     * @param mac 맥 주소
     * @return 맥 주소에 해당하는 별칭
     */
    fun getAlias(mac: String) : Flow<String?>
}