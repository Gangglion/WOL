package com.glion.wol.data.auth

import com.glion.wol.data.api.RefreshTokenApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : TokenAutheenticator
 * Created by glion on 2025-09-24
 *
 * Description:
 * - 401 응답 가로채서 토큰 갱신
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshTokenApi: RefreshTokenApi
) : Authenticator {
    @OptIn(InternalCoroutinesApi::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        val currentToken = runBlocking { tokenManager.getToken() }
        val requestToken = response.request.header("Authorization")?.substringAfter("Bearer ")
        if (currentToken != requestToken) { // 저장된 토큰과 요청할때 사용한 토큰이 다르다면
            return null // 재시도 포기
        }

        // 동시성 문제 방지 : 여러 API 가 동시에 401 을 받아도 토큰 갱신은 한번만 실행
        synchronized(this) {
            // 토큰 재확인
            val newToken = runBlocking { tokenManager.getToken() }
            if (currentToken != newToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            }

            try {
                val tokenRefreshResponse = runBlocking {
                    refreshTokenApi.refreshToken()
                }

                if (tokenRefreshResponse.isSuccessful && tokenRefreshResponse.body() != null) {
                    val refreshedNewToken = tokenRefreshResponse.body()!!.value
                    runBlocking {
                        tokenManager.saveToken(refreshedNewToken)
                    }
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                }
            } catch (e: Exception) {
                // 갱신 실패
            }

            return null
        }
    }
}