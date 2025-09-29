package com.glion.wol.domain.usecase.common

import com.glion.wol.di.ApplicationScopeDefault
import com.glion.wol.domain.repository.FcmRepository
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : TokenSyncUseCase
 * Created by glion on 2025-09-29
 *
 * Description:
 * - 토큰 변화를 관찰하여 토큰이 변경된다면 서버로 전송하는 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Singleton
class TokenSyncUseCase @Inject constructor(
    private val fcmRepository: FcmRepository,
    @ApplicationScopeDefault private val scope: CoroutineScope
) {
    fun startSyncToken() {
        scope.launch {
            fcmRepository.getFcmToken()
                .filterNotNull()
                .collect { token ->
                    LogUtil.d("TokenSyncUseCase :: $token")
                    fcmRepository.sendFcmToken(token)
                }
        }
    }
}