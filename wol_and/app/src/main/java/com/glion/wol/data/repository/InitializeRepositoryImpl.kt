package com.glion.wol.data.repository

import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.crypto.dataSource.CryptoKeyDataSource
import com.glion.wol.domain.repository.InitializeRepository
import com.glion.wol.util.b64DecodeByteArray
import com.glion.wol.util.b64Encode
import java.security.KeyPair
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : InitializeRepositoryImpl
 * Created by glion on 2025-10-13
 *
 * Description:
 * - 앱 초기화 관련 InitializeRepository 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Singleton
class InitializeRepositoryImpl @Inject constructor(
    private val cryptoKeyDs: CryptoKeyDataSource,
    private val apiDs: ApiDataSource
) : InitializeRepository {
    override suspend fun loadOrCreateRSAKey() : KeyPair {
        return cryptoKeyDs.getOrCreateRSAKeyPair()
    }

    override suspend fun saveAESKey(encryptedAESKey: ByteArray) {
        cryptoKeyDs.saveAESKey(encryptedAESKey)
    }

    override suspend fun loadAESKey() : ByteArray {
        return cryptoKeyDs.loadAESKey()
    }

    override suspend fun isExistAESKey(): Boolean {
        return cryptoKeyDs.isExistAESKey()
    }

    override suspend fun exchangeKey(rsaPublicKey: ByteArray): ByteArray {
        val body = RequestExchangeKey(base64EncodedRsaPublicKey = rsaPublicKey.b64Encode())
        return apiDs.exchangeKey(body).value.b64DecodeByteArray()
    }
}