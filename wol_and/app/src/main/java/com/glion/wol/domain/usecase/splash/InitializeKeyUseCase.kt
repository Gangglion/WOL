package com.glion.wol.domain.usecase.splash

import com.glion.wol.domain.repository.CryptoRepository
import com.glion.wol.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Project : WOL
 * File : InitializeKeyUseCase
 * Created by glion on 2025-09-23
 *
 * Description:
 * - 키 준비 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class InitializeKeyUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val cryptoRepository: CryptoRepository
) {
    operator fun invoke() : Flow<Unit> = flow {
        // 1. RSA 키 생성 or 로드 - 먼저 호출하여 끝냄
        cryptoRepository.loadOrCreateRSAKey()

        // 2. 생성된 RSA 키의 공개키 가져오기
        val rsaPublicKey = cryptoRepository.getRSAPublicKey()

        // 3. AES 키 존재 확인
        val isExistAesKey = cryptoRepository.isExistAESKey()

        if(isExistAesKey) {
            // 4-1. AES 키가 존재한다면 메모리에 로드
            cryptoRepository.loadAESKey()
        } else {
            // 4-2. AES 키가 없다면 API 호출 후 저장
            val encryptedAesKey = remoteRepository.exchangeKey(rsaPublicKey)
            cryptoRepository.saveAESKey(encryptedAesKey)
        }

        // 모든 과정 완료 시 결과 emit
        emit(Unit)
    }
}