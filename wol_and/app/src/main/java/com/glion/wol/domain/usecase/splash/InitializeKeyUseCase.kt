package com.glion.wol.domain.usecase.splash

import com.glion.wol.domain.repository.CryptoRepository
import com.glion.wol.domain.repository.RemoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
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
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke() : Flow<Unit> {
        // 1. RSA 키 생성 or 로드 - 먼저 호출하여 끝냄
        return cryptoRepository.loadOrCreateRSAKey()
            .flatMapConcat {
                // 2. 생성된 RSA 키의 공개키 가져오기
                cryptoRepository.getRSAPublicKey()
            }
            // 3. 생성된 RSA 키 가지고 Flow 체인 시작. 결과 리턴
            .flatMapConcat { rsaPublicKeyResult ->
                cryptoRepository.isExistAESKey()
                    .flatMapConcat { keyExists ->
                        if(keyExists) {
                            cryptoRepository.loadAESKey()
                        } else {
                            remoteRepository.exchangeKey(rsaPublicKeyResult)
                                .flatMapConcat { encryptedAESKey ->
                                    cryptoRepository.saveAESKey(encryptedAESKey)
                                }
                        }
                    }
            }
    }
}