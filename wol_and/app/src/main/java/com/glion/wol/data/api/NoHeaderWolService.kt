package com.glion.wol.data.api

import com.glion.wol.data.api.data.RequestEncryptedCommon
import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.data.ResponseEncryptedCommon
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Project : WOL
 * File : NoHeaderWolService
 * Created by glion on 2025-09-24
 *
 * Description:
 * - 헤더가 필요없는 API
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface NoHeaderWolService {
    @POST("auth/exchangeKey")
    suspend fun exchangeKey(@Body body: RequestExchangeKey) : Response<ResponseEncryptedCommon>

    @POST("auth/getToken")
    suspend fun getToken(@Body body: RequestEncryptedCommon) : Response<ResponseEncryptedCommon>
}