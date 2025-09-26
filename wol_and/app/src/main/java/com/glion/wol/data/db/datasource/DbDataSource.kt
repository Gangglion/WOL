package com.glion.wol.data.db.datasource

import com.glion.wol.data.db.entity.DeviceEntity
import kotlinx.coroutines.flow.Flow

/**
 * Project : WOL
 * File : DbDataSource
 * Created by glion on 2025-09-02
 *
 * Description:
 * - DataBase 에서 데이터를 가져오는 역할을 하는 DataSource 정의
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface DbDataSource {
    fun getAllDevice(): Flow<List<DeviceEntity>>

    suspend fun changeMacAddr(id: Long, newMacAddr: String)

    suspend fun changeAlias(id: Long, newAlias: String)

    suspend fun changePowerStatus(macAddr: String, status: Boolean)

    suspend fun insertDevice(vararg deviceEntities: DeviceEntity)

    suspend fun deleteDevice(deviceEntity: DeviceEntity): Int
}