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
     */
    suspend fun loadOrCreateRSAKey() : Flow<Unit>

    /**
     * RSA 공개키 리턴
     * @return 성공여부
     */
    fun getRSAPublicKey() : Flow<ByteArray>

    /**
     * AES 키 파일로 저장 - IO
     * @param encryptedAESKey RSA로 암호화된 AES 키 Flow
     * @return 성공여부
     */
    suspend fun saveAESKey(encryptedAESKey: ByteArray) : Flow<Unit>


    /**
     * AES 키 메모리에 로드
     */
    fun loadAESKey() : Flow<Unit>

    /**
     * AES 키 존재 여부 리턴
     * @return 성공여부
     */
    fun isExistAESKey() : Flow<Boolean>

    /**
     * RSA 복호화 - Default
     * @param encrypted 복호화 할 ByteArray
     * @return 복호화 된 ByteArray Flow
     */
    fun decryptAES(encrypted: ByteArray) : Flow<ByteArray>

    /**
     * AES 암호화 - Main
     * @param origin 암호화 할 String
     * @return 암호화된 ByteArray, iv ByteArray Pair Flow
     */
    fun encryptAES(origin: String) : Flow<Pair<ByteArray, ByteArray>>

    /**
     * AES 복호화 - Main
     * @return 복호화된 문자열
     */
    fun decryptAES(encrypted: ByteArray, iv: ByteArray) : Flow<String>
}