package com.glion.wol.data.repository

import com.glion.crypto_module.AESUtils
import com.glion.wol.data.api.data.RequestEncryptedCommon
import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.crypto.dataSource.CryptoKeyDataSource
import com.glion.wol.data.datastore.datasource.SettingDataSource
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.remote.CommonResult
import com.glion.wol.domain.repository.PushRepository
import com.glion.wol.util.b64Encode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Project : WOL
 * File : PushRepositoryImpl
 * Created by glion on 2025-10-13
 *
 * Description:
 * - PushRepository 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class PushRepositoryImpl @Inject constructor(
    private val settingDs: SettingDataSource,
    private val cryptoKeyDs: CryptoKeyDataSource,
    private val apiDs: ApiDataSource,
    private val aesUtils: AESUtils
) : PushRepository {
    override fun getFcmToken(): Flow<String?> = settingDs.fcmToken

    override suspend fun saveFcmToken(fcmToken: String) {
        settingDs.setFcmToken(fcmToken)
    }

    override suspend fun sendFcmToken(fcmToken: String): CommonResult {
        val aesKey = cryptoKeyDs.loadAESKey()
        val encrypted = aesUtils.encrypt(aesKey, fcmToken)
        val b64EncodedFcmToken = encrypted.first.b64Encode()
        val b64EncodedIv = encrypted.second.b64Encode()
        val body = RequestEncryptedCommon(
            encryptedDataBase64 = b64EncodedFcmToken,
            ivBase64 = b64EncodedIv
        )
        return apiDs.sendPushToken(body).toModel()
    }

    override suspend fun decryptedPushData(encryptedData: ByteArray, iv: ByteArray): String {
        val aesKey = cryptoKeyDs.loadAESKey()
        return aesUtils.decryptToString(aesKey, encryptedData, iv)
    }
}