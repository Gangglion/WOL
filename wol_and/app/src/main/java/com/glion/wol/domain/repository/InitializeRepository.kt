package com.glion.wol.domain.repository

import java.security.KeyPair

/**
 * Project : WOL
 * File : InitializeRepository
 * Created by glion on 2025-10-13
 *
 * Description:
 * - 앱 초기화 관련 Repository
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface InitializeRepository {
    /**
     * RSA 키 로드(없으면 생성)
     * @return RSA KeyPair
     */
    suspend fun loadOrCreateRSAKey() : KeyPair

    /**
     * AES 키 파일로 저장
     * @param encryptedAESKey RSA로 암호화된 AES 키
     * @return Unit
     */
    suspend fun saveAESKey(encryptedAESKey: ByteArray)


    /**
     * AES 키 메모리에 로드
     * @return AesKey
     */
    suspend fun loadAESKey() : ByteArray

    /**
     * AES 키 존재 여부 리턴
     * @return 성공여부
     */
    suspend fun isExistAESKey() : Boolean

    /**
     * 키 교환 (RSAPublicKey -> AESKey)
     * @param rsaPublicKey RSA Public Key
     * @return 암호화된 AESKey
     */
    suspend fun exchangeKey(rsaPublicKey: ByteArray) : ByteArray
}