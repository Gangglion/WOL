package com.glion.wol.domain.usecase.splash

import com.glion.wol.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    private val authRepository: AuthRepository
) {
    operator fun invoke() : Flow<Unit> = flow {
        authRepository.getAccessToken()
        emit(Unit)
    }.flowOn(Dispatchers.IO)
}