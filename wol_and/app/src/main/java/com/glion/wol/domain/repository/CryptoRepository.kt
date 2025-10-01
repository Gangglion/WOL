package com.glion.wol.domain.repository

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
    suspend fun loadOrCreateRSAKey()

    /**
     * RSA 공개키 리턴
     * @return RSA 공개키
     */
    suspend fun getRSAPublicKey() : ByteArray

    /**
     * AES 키 파일로 저장 - IO
     * @param encryptedAESKey RSA로 암호화된 AES 키 Flow
     * @return Unit
     */
    suspend fun saveAESKey(encryptedAESKey: ByteArray)


    /**
     * AES 키 메모리에 로드
     * @return Unit
     */
    suspend fun loadAESKey()

    /**
     * AES 키 존재 여부 리턴
     * @return 성공여부
     */
    suspend fun isExistAESKey() : Boolean

    /**
     * AES 암호화된 MAC 주소값 복호화
     * @param encryptedValue 암호화된 값
     * @param iv iv 값
     */
    suspend fun decryptedMac(encryptedValue: ByteArray, iv: ByteArray) : String
}