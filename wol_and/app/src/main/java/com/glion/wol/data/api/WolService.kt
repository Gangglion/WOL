package com.glion.wol.data.api

import com.glion.wol.data.api.data.RequestExchangeKey
import com.glion.wol.data.api.data.RequestJwtToken
import com.glion.wol.data.api.data.RequestWolStart
import com.glion.wol.data.api.data.ResponseExchangeKey
import com.glion.wol.data.api.data.ResponseJwtToken
import com.glion.wol.data.api.data.ResponseWolStart
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Project : WOL
 * File : Api
 * Created by glion on 2025-09-22
 *
 * Description:
 * - API Interface
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface WolService {
    @POST("auth/exchangeKey")
    suspend fun exchangeKey(@Body body: RequestExchangeKey) : Response<ResponseExchangeKey>

    @POST("auth/getToken")
    suspend fun getJwtToken(@Body body: RequestJwtToken) : Response<ResponseJwtToken>

    @POST("auth/refreshToken")
    suspend fun refreshJwtToken(@Body body: String = "") : Response<ResponseJwtToken>

    @POST("wol/start")
    suspend fun startDevice(@Body body: RequestWolStart): Response<ResponseWolStart>
}
