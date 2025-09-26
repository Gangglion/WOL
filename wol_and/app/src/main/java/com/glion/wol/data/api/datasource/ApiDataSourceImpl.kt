package com.glion.wol.data.api.datasource

import com.glion.crypto_module.encryptExternalAES
import com.glion.wol.BuildConfig
import com.glion.wol.data.api.NeedHeaderWolService
import com.glion.wol.data.api.NoHeaderWolService
import com.glion.wol.data.api.RefreshTokenApi
import com.glion.wol.data.api.data.RequestEncryptedCommon
import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.data.RequestToken
import com.glion.wol.data.api.data.ResponseCommon
import com.glion.wol.data.api.data.ResponseExchangeKey
import com.glion.wol.data.api.data.ResponseToken
import com.glion.wol.util.b64Encode
import javax.inject.Inject

/**
 * Project : WOL
 * File : ApiDataSourceImpl
 * Created by glion on 2025-09-22
 *
 * Description:
 * - ApiDataSource 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class ApiDataSourceImpl @Inject constructor(
    private val noHeaderApi: NoHeaderWolService,
    private val needHeaderApi: NeedHeaderWolService,
    private val refreshTokenApi: RefreshTokenApi
) : ApiDataSource {
    override suspend fun exchangeKey(body: RequestExchangeKey): ResponseExchangeKey {
        val response = noHeaderApi.exchangeKey(body)
        if(response.isSuccessful) {
            return response.body() ?: throw NullPointerException("Body is Null")
        } else {
            throw Exception("Network Error :: $response")
        }
    }

    override suspend fun getToken(): ResponseToken {
        val aesEncryptedAppKey = (BuildConfig.APP_KEY).encryptExternalAES()
        val b64EncodedAppKey = aesEncryptedAppKey.first.b64Encode()
        val b64EncodedIv = aesEncryptedAppKey.second.b64Encode()
        val body = RequestToken(appKey = b64EncodedAppKey, iv = b64EncodedIv)
        val response = noHeaderApi.getToken(body)
        if(response.isSuccessful) {
            return response.body() ?: throw NullPointerException("Body is Null")
        } else {
            throw Exception("Network Error :: $response")
        }
    }

    override suspend fun refreshToken(): ResponseToken {
        val response = refreshTokenApi.refreshToken()
        if(response.isSuccessful) {
            return response.body() ?: throw NullPointerException("Body is Null")
        } else {
            throw Exception("Network Error :: $response")
        }
    }

    override suspend fun sendPushToken(fcmToken: String): ResponseCommon {
        val encrypted = fcmToken.encryptExternalAES()
        val b64EncodedFcmToken = encrypted.first.b64Encode()
        val b64EncodedIv = encrypted.second.b64Encode()
        val body = RequestEncryptedCommon(
            encryptedData = b64EncodedFcmToken,
            iv = b64EncodedIv
        )
        val response = needHeaderApi.sendPushToken(body)
        if(response.isSuccessful) {
            return response.body() ?: throw NullPointerException("Body is Null")
        } else {
            throw Exception("Network Error :: $response")
        }
    }

    override suspend fun startDevice(body: RequestEncryptedCommon): ResponseCommon {
        val response = needHeaderApi.startDevice(body)
        if(response.isSuccessful) {
            return response.body() ?: throw NullPointerException("Body is Null")
        } else {
            throw Exception("Network Error :: $response")
        }
    }
}