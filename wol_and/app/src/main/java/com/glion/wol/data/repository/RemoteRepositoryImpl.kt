package com.glion.wol.data.repository

import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.data.RequestWolStart
import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.remote.WolResult
import com.glion.wol.domain.repository.RemoteRepository
import com.glion.wol.util.b64DecodeByteArray
import com.glion.wol.util.b64Encode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    override suspend fun exchangeKey(rsaPublicKey: ByteArray): Flow<ByteArray> = flow {
        val request = RequestExchangeKey(base64EncodedRsaPublicKey = rsaPublicKey.b64Encode())
        // 키 교환 API 호출
        val body = apiDs.exchangeKey(request)
        // body 로 넘어온 RSA 키로 암호화된 AES 키 base64 디코딩
        val encryptedAesKey = body.value.b64DecodeByteArray()
        // 암호화된 키 값 그대로 리턴
        emit(encryptedAesKey)
    }.flowOn(Dispatchers.IO)

    override suspend fun getToken(): Flow<String> = flow {
        val body = apiDs.getToken()
        emit(body.value)
    }.flowOn(Dispatchers.IO)

    override suspend fun refreshToken(): Flow<String> = flow {
        val body = apiDs.refreshToken()
        emit(body.value)
    }.flowOn(Dispatchers.IO)

    override suspend fun startDevice(mac: String, iv: String): Flow<WolResult> = flow {
        val request = RequestWolStart(
            encryptedData = mac,
            iv = iv
        )
        val body = apiDs.startDevice(request).toModel()
        emit(body)
    }.flowOn(Dispatchers.IO)
}