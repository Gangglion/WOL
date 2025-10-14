package com.glion.wol.domain.repository

/**
 * Project : WOL
 * File : AuthRepository
 * Created by glion on 2025-10-13
 *
 * Description:
 * - 사용자 인증 관련 Repository
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface AuthRepository {
    /**
     * AccessToken 얻기
     */
    suspend fun getAccessToken()
}