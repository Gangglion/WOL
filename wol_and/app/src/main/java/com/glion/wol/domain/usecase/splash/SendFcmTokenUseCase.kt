package com.glion.wol.domain.usecase.splash

import com.glion.wol.domain.repository.PushRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Project : WOL
 * File : SendFcmTokenUseCase
 * Created by glion on 2025-10-01
 *
 * Description:
 * - FCM 토큰 서버로 전송하는 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class SendFcmTokenUseCase @Inject constructor(
    private val pushRepository: PushRepository
) {
    operator fun invoke() : Flow<Unit> {
        return pushRepository.getFcmToken()
            .filterNotNull()
            .map { fcmToken ->
                val sendResult = pushRepository.sendFcmToken(fcmToken)
                if(sendResult.result) {
                    Unit
                } else {
                    throw Exception("Fail to send FCM Token :: ${sendResult.message}")
                }
            }
    }
}