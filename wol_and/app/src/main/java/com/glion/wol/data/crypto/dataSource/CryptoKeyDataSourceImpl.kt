package com.glion.wol.data.crypto.dataSource

import android.content.Context
import com.glion.crypto_module.RSAUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyPair
import javax.inject.Inject
import javax.inject.Singleton

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
@Singleton
class CryptoKeyDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val rsaUtils: RSAUtils
) : CryptoKeyDataSource {
    @Volatile
    var cachedRsaKey: KeyPair? = null
    private set

    @Volatile
    var cachedAesKey: ByteArray? = null
    private set

    override suspend fun getOrCreateRSAKeyPair(): KeyPair {
        // 캐시된 값이 있을 경우 먼저 리턴
        cachedRsaKey?.let { return it }
        return withContext(Dispatchers.Default) {
            val rsaKey = rsaUtils.getOrCreateRSAKeyPair()
            cachedRsaKey = rsaKey
            rsaKey
        }
    }

    override suspend fun loadAESKey(): ByteArray {
        // 캐시된 값이 있다면 먼저 리턴
        cachedAesKey?.let { return it }
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, "aesKey")
            if(!isExistAESKey()) throw Exception("저장된 AES 키 파일이 없습니다")
            val encryptedAesKeyBytes = FileInputStream(file).use { it.readBytes() }
            val privateKey = cachedRsaKey?.private ?: rsaUtils.getOrCreateRSAKeyPair().private
            val decryptedAesKey = rsaUtils.decryptByteArray(privateKey, encryptedAesKeyBytes)
            cachedAesKey = decryptedAesKey
            decryptedAesKey
        }
    }

    override suspend fun saveAESKey(encryptedAesKey: ByteArray) {
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, "aesKey")
            FileOutputStream(file).use { it.write(encryptedAesKey) }
        }
    }

    /**
     * 앱 내부저장소에 AES 키 파일 있는지 확인
     */
    override fun isExistAESKey() = File(context.filesDir, "aesKey").exists()
}