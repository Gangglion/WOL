package com.glion.wol.data.crypto.dataSource

import java.security.KeyPair

/**
 * Project : WOL
 * File : CryptoDataSourrce
 * Created by glion on 2025-09-23
 *
 * Description:
 * - 암호화 관련 DataSource
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface CryptoKeyDataSource {
    /**
     * RSA 키 리턴(없으면 생성해서 리턴)
     */
    suspend fun getOrCreateRSAKeyPair() : KeyPair

    /**
     * AES 키 리턴(없으면 예외)
     */
    suspend fun loadAESKey(): ByteArray

    /**
     * AES 키 저장
     * @param encryptedAesKey RSA 로 암호화된 AES 키
     */
    suspend fun saveAESKey(encryptedAesKey: ByteArray)

    /**
     * 앱 내부 저장소에 AES키 존재 여부 리턴
     * @return 키 존재여부(true : 존재 / false : 없음)
     */
    fun isExistAESKey() : Boolean
}