package com.glion.wol.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Project : WOL
 * File : CryptoRepository
 * Created by glion on 2025-09-23
 *
 * Description:
 * - 암호화 관련 Repository
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface CryptoRepository {
    /**
     * RSA 키 로드(없으면 생성) - Default
     * @return Unit
     */
    fun loadOrCreateRSAKey() : Flow<Unit>

    /**
     * RSA 공개키 리턴
     * @return RSA 공개키
     */
    fun getRSAPublicKey() : Flow<ByteArray>

    /**
     * AES 키 파일로 저장 - IO
     * @param encryptedAESKey RSA로 암호화된 AES 키 Flow
     * @return Unit
     */
    suspend fun saveAESKey(encryptedAESKey: ByteArray) : Flow<Unit>


    /**
     * AES 키 메모리에 로드
     * @return Unit
     */
    fun loadAESKey() : Flow<Unit>

    /**
     * AES 키 존재 여부 리턴
     * @return 성공여부
     */
    fun isExistAESKey() : Flow<Boolean>
}