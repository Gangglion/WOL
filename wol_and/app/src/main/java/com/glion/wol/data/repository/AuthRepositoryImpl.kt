package com.glion.wol.data.repository

import com.glion.crypto_module.AESUtils
import com.glion.wol.BuildConfig
import com.glion.wol.data.api.data.RequestEncryptedCommon
import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.auth.TokenManager
import com.glion.wol.data.crypto.dataSource.CryptoKeyDataSource
import com.glion.wol.domain.repository.AuthRepository
import com.glion.wol.util.b64Encode
import javax.inject.Inject

/**
 * Project : WOL
 * File : AuthRepositoryImpl
 * Created by glion on 2025-10-13
 *
 * Description:
 * - 사용자 인증 관련 Repository 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class AuthRepositoryImpl @Inject constructor(
    private val tokenManager: TokenManager,
    private val cryptoKeyDs: CryptoKeyDataSource,
    private val apiDs: ApiDataSource,
    private val aesUtils: AESUtils
) : AuthRepository {

    override suspend fun getAccessToken() {
        if(tokenManager.getToken() == null) {
            val aesKey = cryptoKeyDs.loadAESKey()
            val aesEncryptedAppKey = aesUtils.encrypt(aesKey, BuildConfig.APP_KEY)
            val b64EncodedAppKey = aesEncryptedAppKey.first.b64Encode()
            val b64EncodedIv = aesEncryptedAppKey.second.b64Encode()
            val body = RequestEncryptedCommon(
                encryptedDataBase64 = b64EncodedAppKey,
                ivBase64 = b64EncodedIv
            )
            val newAccessToken = apiDs.getToken(body).value
            tokenManager.saveToken(newAccessToken)
        }
    }
}