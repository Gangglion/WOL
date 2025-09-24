package com.glion.wol.data.api.data

/**
 * Project : WOL
 * File : ResponseWolStart
 * Created by glion on 2025-09-22
 *
 * Description:
 * - WOL API Response
 * @param result 결과 - 매직패킷 전송 성공했으면 true, 아니면 false(전원 켜짐과 관계 없음)
 * @param message 결과 메시지
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class ResponseWolStart(
    val result: Boolean,
    val message: String
)
