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
            // 키 교환 API 호출 -> 결과를 Base64 로 디코딩
            apiDs.exchangeKey(rsaPublicKey).value.b64DecodeByteArray()
        }
    }

    override suspend fun getToken(): String {
        return withContext(Dispatchers.IO) {
            apiDs.getToken().value
        }
    }

    override suspend fun refreshToken(): String {
        return withContext(Dispatchers.IO) {
            apiDs.refreshToken().value
        }
    }

    override suspend fun startDevice(mac: String): CommonResult {
        return withContext(Dispatchers.IO) {
            apiDs.startDevice(mac).toModel()
        }
    }

    override suspend fun sendFcmToken(fcmToken: String): CommonResult {
        return withContext(Dispatchers.IO) {
            apiDs.sendPushToken(fcmToken).toModel()
        }
    }
}