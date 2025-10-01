package com.glion.wol.data.repository

import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.remote.CommonResult
import com.glion.wol.domain.repository.RemoteRepository
import com.glion.wol.util.b64DecodeByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : RemoteRepositoryImpl
 * Created by glion on 2025-09-22
 *
 * Description:
 * - API Repository 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Singleton
class RemoteRepositoryImpl @Inject constructor(
    private val apiDs: ApiDataSource
) : RemoteRepository {
    override suspend fun exchangeKey(rsaPublicKey: ByteArray): ByteArray {
        return withContext(Dispatchers.IO) {
            // 키 교환 API 호출
            val body = apiDs.exchangeKey(rsaPublicKey)
            // body 로 넘어온 RSA 키로 암호화된 AES 키 base64 디코딩
            val encryptedAesKey = body.value.b64DecodeByteArray()
            // 암호화된 키 값 그대로 리턴
            encryptedAesKey
        }
    }

    override suspend fun getToken(): String {
        return withContext(Dispatchers.IO) {
            val body = apiDs.getToken()
            body.value
        }
    }

    override suspend fun refreshToken(): String {
        return withContext(Dispatchers.IO) {
            val body = apiDs.refreshToken()
            body.value
        }
    }

    override suspend fun startDevice(mac: String): CommonResult {
        return withContext(Dispatchers.IO) {
            val body = apiDs.startDevice(mac).toModel()
            body
        }
    }
}