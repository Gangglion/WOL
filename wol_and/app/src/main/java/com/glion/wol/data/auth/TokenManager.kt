package com.glion.wol.data.auth

import com.glion.wol.data.datastore.datasource.SettingDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : TokenManager
 * Created by glion on 2025-09-24
 *
 * Description:
 * - Header 토큰 관리
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Singleton
class TokenManager @Inject constructor(
    private val settingDataSource: SettingDataSource
) {
    // 토큰값 메모리에 캐시
    @Volatile
    private var authToken: String? = null

    // 여러 스레드에서 동시 접근 방지
    private val mutex = Mutex()

    /**
     * 저장된 토큰 일회성으로 가져옴
     * API 요청 헤더에 토큰을 추가하는 등 현재 토큰값이 한 번만 필요할 때 사용
     * Flow 의 첫번째 값을 가져오며, 없으면 null 반환
     *
     * @return 저장된 토큰, 없으면 null
     */
    suspend fun getToken(): String? {
        if(authToken != null)
            return authToken

        // 캐시된 토큰값이 없을 경우, 초기화 로직 실행
        mutex.withLock { // 다른 스레드가 뮤텍스를 기다리는동안 토큰 초기화가 이루어졌을 수 있으니 확인
            if(authToken == null) {
                authToken = settingDataSource.token.firstOrNull()
            }
            return authToken
        }
    }

    /**
     * 새로운 토큰 저장
     * @param 저장될 토큰
     */
    suspend fun saveToken(token: String) {
        mutex.withLock {
            authToken = token
            settingDataSource.setToken(token)
        }
    }
}