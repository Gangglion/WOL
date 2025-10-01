package com.glion.wol.domain.usecase.splash

import com.glion.wol.data.auth.TokenManager
import com.glion.wol.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Project : WOL
 * File : GetTokenUseCase
 * Created by glion on 2025-09-23
 *
 * Description:
 * - 토큰 존재 여부 확인 후 토큰 요청
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class GetTokenUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val tokenManager: TokenManager
) {
    operator fun invoke() : Flow<Unit> = flow {
        if(tokenManager.getToken() == null) { // 저장된 토큰이 없을때만 요청
            val newToken = remoteRepository.getToken()
            tokenManager.saveToken(newToken)
        }
        emit(Unit)
    }
}