package com.glion.wol.data.api

import com.glion.wol.data.api.data.RequestWolStart
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
interface NeedHeaderWolService {
    @POST("wol/start")
    suspend fun startDevice(@Body body: RequestWolStart): Response<ResponseWolStart>
}
