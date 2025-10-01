package com.glion.wol.data.repository

import com.glion.wol.data.crypto.dataSource.CryptoDataSource
import com.glion.wol.domain.repository.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : CryptoRepositoryImpl
 * Created by glion on 2025-09-23
 *
 * Description:
 * - 암호화 키 관련 Repository
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Singleton
class CryptoRepositoryImpl @Inject constructor(
    private val dataSource: CryptoDataSource
) : CryptoRepository {
    override suspend fun loadOrCreateRSAKey() {
        withContext(Dispatchers.Default) {
            dataSource.loadOrCreateRSAKey()
        }
    }

    override suspend fun getRSAPublicKey(): ByteArray {
        return withContext(Dispatchers.Default) {
            dataSource.getRsaPublicKey()
        }
    }

    override suspend fun saveAESKey(encryptedAESKey: ByteArray) {
        withContext(Dispatchers.Default) {
            dataSource.saveAESKey(encryptedAESKey)
        }
    }

    override suspend fun loadAESKey() {
        withContext(Dispatchers.Default) {
            dataSource.loadAESKey()
        }
    }

    override suspend fun isExistAESKey(): Boolean {
        return withContext(Dispatchers.Default) {
            dataSource.isExistAESKey()
        }
    }
}