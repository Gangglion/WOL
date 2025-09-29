package com.glion.wol.data.repository

import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.crypto.dataSource.CryptoDataSource
import com.glion.wol.data.datastore.datasource.SettingDataSource
import com.glion.wol.data.db.datasource.DbDataSource
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.remote.CommonResult
import com.glion.wol.domain.repository.FcmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Project : WOL
 * File : FcmRepositoryImpl
 * Created by glion on 2025-09-26
 *
 * Description:
 * - FCM 에서 사용할 Repository
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class FcmRepositoryImpl @Inject constructor(
    private val dbDataSource: DbDataSource,
    private val apiDataSource: ApiDataSource,
    private val cryptoDataSource: CryptoDataSource,
    private val settingDataSource: SettingDataSource
) : FcmRepository {
    override fun getFcmToken(): Flow<String?> = settingDataSource.fcmToken

    override suspend fun saveFcmToken(fcmToken: String) {
        settingDataSource.setFcmToken(fcmToken)
    }

    /**
     * 푸시토큰 서버로 전송
     */
    override suspend fun sendFcmToken(fcmToken: String): CommonResult {
        val body = apiDataSource.sendPushToken(fcmToken).toModel()
        return body
    }

    override suspend fun decryptedMac(encryptedValue: ByteArray, iv: ByteArray): String = cryptoDataSource.decryptAES(encryptedValue, iv)

    /**
     * 받은 푸시메시지에 등록된 Mac 주소에 따라 전원 상태 변경
     */
    override suspend fun changePowerStatus(macAddr: String, status: Boolean) = dbDataSource.changePowerStatus(macAddr, status)

    /**
     * 맥 주소에 따른 별칭 리턴
     */
    override suspend fun getAlias(mac: String): String? = dbDataSource.getAlias(mac)
}