package com.glion.wol.data.api

import com.glion.wol.data.api.data.ResponseToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Project : WOL
 * File : RefreshTokenApi
 * Created by glion on 2025-09-24
 *
 * Description:
 * - 토큰 갱신 API
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface RefreshTokenApi {
    @POST("auth/refreshToken")
    suspend fun refreshToken(@Body body: String = "") : Response<ResponseToken>
}