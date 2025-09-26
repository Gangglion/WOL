package com.glion.wol.data.repository

import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.db.datasource.DbDataSource
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.remote.CommonResult
import com.glion.wol.domain.repository.FcmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    private val apiDataSource: ApiDataSource
) : FcmRepository {
    /**
     * 푸시토큰 서버로 전송
     */
    override suspend fun sendFcmToken(fcmToken: String): Flow<CommonResult> = flow {
        val body = apiDataSource.sendPushToken(fcmToken).toModel()
        emit(body)
    }.flowOn(Dispatchers.IO)

    /**
     * 받은 푸시메시지에 등록된 Mac 주소에 따라 전원 상태 변경
     */
    override suspend fun changePowerStatus(macAddr: String, status: Boolean): Flow<Unit> = flow {
        dbDataSource.changePowerStatus(macAddr, status)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    /**
     * 맥 주소에 따른 별칭 리턴
     */
    override fun getAlias(mac: String): Flow<String?> = flow {
        val alias = dbDataSource.getAlias(mac)
        emit(alias)
    }.flowOn(Dispatchers.IO)
}