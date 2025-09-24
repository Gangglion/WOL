package com.glion.wol.data.crypto.dataSource

import com.glion.crypto_module.ExternalAESUtils
import com.glion.crypto_module.RSAUtils
import com.glion.crypto_module.decryptExternalAES
import com.glion.crypto_module.decryptRSAByteArray
import com.glion.crypto_module.decryptRSAStr
import com.glion.crypto_module.encryptExternalAES
import com.glion.crypto_module.encryptRSA
import javax.inject.Inject

/**
 * Project : WOL
 * File : CryptoDataSourceImpl
 * Created by glion on 2025-09-23
 *
 * Description:
 * - 암호화 관련 DataSource 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class CryptoDataSourceImpl @Inject constructor() : CryptoDataSource {
    override fun loadOrCreateRSAKey() {
        RSAUtils.getOrCreateRSAKeyPair()
    }

    override fun getRsaPublicKey(): ByteArray = RSAUtils.getRsaPublicKey()

    override fun saveAESKey(encryptedAesKey: ByteArray) {
        ExternalAESUtils.saveAESKey(encryptedAesKey)
    }

    override fun loadAESKey() {
        ExternalAESUtils.getAesKeyFromFile()
    }

    override fun isExistAESKey(): Boolean = ExternalAESUtils.isExistAESKeyFile()

    override fun encryptRSA(origin: ByteArray): ByteArray = origin.encryptRSA()

    override fun encryptRSA(origin: String): ByteArray = origin.encryptRSA()

    override fun decryptRSAStr(encrypted: ByteArray): String = encrypted.decryptRSAStr()

    override fun decryptRSAByteArray(encrypt: ByteArray): ByteArray = encrypt.decryptRSAByteArray()

    override fun encryptAES(origin: String): Pair<ByteArray, ByteArray> = origin.encryptExternalAES()

    override fun decryptAES(encrypted: ByteArray, iv: ByteArray): String = (encrypted to iv).decryptExternalAES()
}