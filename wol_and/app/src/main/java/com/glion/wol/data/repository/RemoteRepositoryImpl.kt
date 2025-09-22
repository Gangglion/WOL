package com.glion.wol.data.repository

import com.glion.crypto_module.encryptExternalAES
import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.data.RequestWolStart
import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.remote.WolResult
import com.glion.wol.domain.repository.RemoteRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.b64DecodeByteArray
import com.glion.wol.util.b64Encode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

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
class RemoteRepositoryImpl @Inject constructor(
    private val apiDs: ApiDataSource
) : RemoteRepository {
    override suspend fun exchangeKey(rsaPublicKey: ByteArray): Flow<FlowResult<ByteArray>> = flow {
        try {
            val request = RequestExchangeKey(base64EncodedRsaPublicKey = rsaPublicKey.b64Encode())
            val body = apiDs.exchangeKey(request)
            val encryptedAesKey = body.value.b64DecodeByteArray()
            emit(FlowResult.Success(encryptedAesKey))
        } catch(e: Exception) {
            e.printStackTrace()
            emit(FlowResult.Error(errorCode = "", errorMsg = ""))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getJwtToken(): Flow<FlowResult<String>> = flow{
        try {
            val body = apiDs.getJwtToken()
            emit(FlowResult.Success(body.value))
        } catch(e: Exception) {
            e.printStackTrace()
            emit(FlowResult.Error(errorCode = "", errorMsg = ""))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun refreshJwtToken(): Flow<FlowResult<String>> = flow {
        try {
            val body = apiDs.refreshJwtToken()
            emit(FlowResult.Success(body.value))
        } catch(e: Exception) {
            e.printStackTrace()
            emit(FlowResult.Error(errorCode = "", errorMsg = ""))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun startDevice(mac: String, iv: String): Flow<FlowResult<WolResult>> = flow {
        try {
            val encrypted = mac.encryptExternalAES()
            val request = RequestWolStart(
                mac = encrypted.first.b64Encode(),
                iv = encrypted.second.b64Encode()
            )
            val body = apiDs.startDevice(request).toModel()
            emit(FlowResult.Success(body))
        } catch(e: Exception) {
            e.printStackTrace()
            emit(FlowResult.Error(errorCode = "", errorMsg = ""))
        }
    }.flowOn(Dispatchers.IO)
}