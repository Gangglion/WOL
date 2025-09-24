package com.glion.wol.data.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Project : WOL
 * File : AuthInterceptor
 * Created by glion on 2025-09-24
 *
 * Description:
 * - 헤더에 Token 넣는 Interceptor
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getToken()
        }

        val newRequest = chain.request().newBuilder().apply {
            if(!token.isNullOrBlank()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}