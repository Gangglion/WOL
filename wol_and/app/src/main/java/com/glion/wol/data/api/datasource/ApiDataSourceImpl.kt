package com.glion.wol.data.api.datasource

import com.glion.crypto_module.encryptExternalAES
import com.glion.wol.BuildConfig
import com.glion.wol.data.api.WolService
import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.data.RequestJwtToken
import com.glion.wol.data.api.data.RequestWolStart
import com.glion.wol.data.api.data.ResponseExchangeKey
import com.glion.wol.data.api.data.ResponseJwtToken
import com.glion.wol.data.api.data.ResponseWolStart
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
    private val api: WolService
) : ApiDataSource {
    override suspend fun exchangeKey(body: RequestExchangeKey): ResponseExchangeKey {
        val response = api.exchangeKey(body)
        if(response.isSuccessful) {
            return response.body() ?: throw NullPointerException("Body is Null")
        } else {
            throw Exception("Network Error :: $response")
        }
    }

    override suspend fun getJwtToken(): ResponseJwtToken {
        val aesEncryptedAppKey = (BuildConfig.APP_KEY).encryptExternalAES()
        val b64EncodedAppKey = aesEncryptedAppKey.first.b64Encode()
        val b64EncodedIv = aesEncryptedAppKey.second.b64Encode()
        val body = RequestJwtToken(appKey = b64EncodedAppKey, iv = b64EncodedIv)
        val response = api.getJwtToken(body)
        if(response.isSuccessful) {
            return response.body() ?: throw NullPointerException("Body is Null")
        } else {
            throw Exception("Network Error :: $response")
        }
    }

    override suspend fun refreshJwtToken(): ResponseJwtToken {
        val response = api.refreshJwtToken()
        if(response.isSuccessful) {
            return response.body() ?: throw NullPointerException("Body is Null")
        } else {
            throw Exception("Network Error :: $response")
        }
    }

    override suspend fun startDevice(body: RequestWolStart): ResponseWolStart {
        val response = api.startDevice(body)
        if(response.isSuccessful) {
            return response.body() ?: throw NullPointerException("Body is Null")
        } else {
            throw Exception("Network Error :: $response")
        }
    }
}