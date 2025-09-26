package com.glion.wol.data.db.datasource

import com.glion.wol.data.db.dao.DeviceDao
import com.glion.wol.data.db.entity.DeviceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Project : WOL
 * File : DbDataSourceImpl
 * Created by glion on 2025-09-02
 *
 * Description:
 * - Database DataSource 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class DbDataSourceImpl @Inject constructor(
    private val dao: DeviceDao
) : DbDataSource {
    override fun getAllDevice(): Flow<List<DeviceEntity>> {
        return dao.getAllDevice()
    }

    override suspend fun changeMacAddr(id: Long, newMacAddr: String) {
        dao.changeMacAddr(id, newMacAddr)
    }

    override suspend fun changeAlias(id: Long, newAlias: String) {
        dao.changeAlias(id, newAlias)
    }

    override suspend fun changePowerStatus(macAddr: String, status: Boolean) {
        dao.changePowerStatus(macAddr, status)
    }

    override suspend fun insertDevice(vararg deviceEntities: DeviceEntity) {
        dao.insertDevice(*deviceEntities)
    }

    override suspend fun deleteDevice(deviceEntity: DeviceEntity): Int {
        return dao.deleteDevice(deviceEntity)
    }
}