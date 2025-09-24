package com.glion.wol.data.auth

import com.glion.wol.data.api.RefreshTokenApi
import com.glion.wol.util.LogUtil
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
        // 현재 요청할때 사용한 토큰
        val currentToken = runBlocking { tokenManager.getToken() }
        val requestToken = response.request.header("Authorization")?.substringAfter("Bearer ")
        if (currentToken != requestToken) { // 저장된 토큰과 요청할때 사용한 토큰이 다르다면
            return null // 재시도 포기
        }

        // 동시성 문제 방지 : 여러 API 가 동시에 401 을 받아도 토큰 갱신은 한번만 실행
        synchronized(this) {
            // 갱신 전 이전에 다른 스레드에서 갱신헀을 수 있으니 확인
            val newToken = runBlocking { tokenManager.getToken() }
            // 요청에 사용된 토큰과 방금 가져온 토큰이 같은지 확인 - 다르면 방금 가져온 토큰으로 재요청
            if (currentToken != newToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            }

            try {
                // 토큰 갱신 요청 - 동기식
                val tokenRefreshResponse = runBlocking {
                    refreshTokenApi.refreshToken()
                }

                // 응답이 성공이고, body 가 비어있지 않을 때
                if (tokenRefreshResponse.isSuccessful && tokenRefreshResponse.body() != null) {
                    // 새로 발급받은 토큰
                    val refreshedNewToken = tokenRefreshResponse.body()!!.value
                    runBlocking {
                        // 새로 발급받은 토큰 DataStore 에 저장
                        tokenManager.saveToken(refreshedNewToken)
                    }
                    // 새로 발급받은 토큰을 넣어 요청 보냄
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $refreshedNewToken")
                        .build()
                }
            } catch (e: Exception) {
                // 갱신 실패
            }

            return null
        }
    }
}