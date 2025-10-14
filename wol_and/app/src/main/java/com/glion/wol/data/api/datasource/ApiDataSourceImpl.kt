package com.glion.wol.data.api.datasource

import com.glion.wol.data.api.NeedHeaderWolService
import com.glion.wol.data.api.NoHeaderWolService
import com.glion.wol.data.api.data.RequestEncryptedCommon
import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.data.ResponseCommon
import com.glion.wol.data.api.data.ResponseEncryptedCommon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

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
@Singleton
class ApiDataSourceImpl @Inject constructor(
    private val noHeaderApi: NoHeaderWolService,
    private val needHeaderApi: NeedHeaderWolService
) : ApiDataSource {
    override suspend fun exchangeKey(body: RequestExchangeKey): ResponseEncryptedCommon {
        return withContext(Dispatchers.IO) {
            val response = noHeaderApi.exchangeKey(body)
            if(response.isSuccessful) {
                response.body() ?: throw NullPointerException("Body is Null")
            } else {
                throw Exception("Network Error :: $response")
            }
        }
    }

    override suspend fun getToken(body: RequestEncryptedCommon): ResponseEncryptedCommon {
        return withContext(Dispatchers.IO) {
            val response = noHeaderApi.getToken(body)
            if (response.isSuccessful) {
                response.body() ?: throw NullPointerException("Body is Null")
            } else {
                throw Exception("Network Error :: $response")
            }
        }
    }


    override suspend fun sendPushToken(body: RequestEncryptedCommon): ResponseCommon {
        return withContext(Dispatchers.IO) {
            val response = needHeaderApi.sendPushToken(body)
            if (response.isSuccessful) {
                response.body() ?: throw NullPointerException("Body is Null")
            } else {
                throw Exception("Network Error :: $response")
            }
        }
    }

    override suspend fun startDevice(body: RequestEncryptedCommon): ResponseCommon {
        return withContext(Dispatchers.IO) {
            val response = needHeaderApi.startDevice(body)
            if (response.isSuccessful) {
                response.body() ?: throw NullPointerException("Body is Null")
            } else {
                throw Exception("Network Error :: $response")
            }
        }
    }
}