package com.glion.wol.data.crypto.dataSource

/**
 * Project : WOL
 * File : CryptoDataSourrce
 * Created by glion on 2025-09-23
 *
 * Description:
 * - 암호화 관련 DataSource
 *
 * Copyright @2025 UBIPLUS. All rights reserved
 */
interface CryptoDataSource {
    /**
     * RSA 키 메모리에 로드 or 없으면 생성하여 메모리에 로드
     */
    fun loadOrCreateRSAKey()

    /**
     * RSA 공개키 리턴
     * @return RSA 공개키
     */
    fun getRsaPublicKey(): ByteArray

    /**
     * AES 키 저장
     * @param encryptedAesKey RSA 로 암호화된 AES 키
     */
    fun saveAESKey(encryptedAesKey: ByteArray)

    /**
     * AES 키 메모리에 로드
     */
    fun loadAESKey()

    /**
     * 앱 내부 저장소에 AES키 존재 여부 리턴
     * @return 키 존재여부(true : 존재 / false : 없음)
     */
    fun isExistAESKey() : Boolean

    /**
     * RSA 로 암호화
     * @param origin 암호화할 원본 ByteArray
     * @return 암호화된 값
     */
    fun encryptRSA(origin: ByteArray) : ByteArray

    /**
     * RSA 로 암호화
     * @param origin 암호화할 원본 String
     * @return 암호화된 값
     */
    fun encryptRSA(origin: String) : ByteArray

    /**
     * RSA 로 암호화된 값 문자열로 복호화
     * @param encrypted 암호화된 ByteArray
     * @return 복호화된 문자열
     */
    fun decryptRSAStr(encrypted: ByteArray) : String

    /**
     * RSA 로 암호화된 값 ByteArray 로 복호화
     * @return 복호화된 ByteArray
     */
    fun decryptRSAByteArray(encrypt: ByteArray) : ByteArray

    /**
     * AES 로 암호화
     * @param origin 암호화 할 원본 String
     * @return 암호화된 값, iv Pair
     */
    fun encryptAES(origin: String) : Pair<ByteArray, ByteArray>

    /**
     * AES 로 복호화
     * @param encrypted 암호화된 ByteArray
     * @param iv iv ByteArray
     * @return 복호화된 문자열
     */
    fun decryptAES(encrypted: ByteArray, iv: ByteArray) : String
}