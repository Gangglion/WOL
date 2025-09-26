package com.glion.wol.data.api

import com.glion.wol.data.api.data.RequestEncryptedCommon
import com.glion.wol.data.api.data.ResponseCommon
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
    @POST("fcm/sendPushToken")
    suspend fun sendPushToken(@Body body: RequestEncryptedCommon) : Response<ResponseCommon>

    @POST("wol/start")
    suspend fun startDevice(@Body body: RequestEncryptedCommon): Response<ResponseCommon>
}
