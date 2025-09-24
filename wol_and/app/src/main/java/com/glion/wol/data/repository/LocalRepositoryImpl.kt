package com.glion.wol.data.repository

import com.glion.wol.data.datastore.datasource.SettingDataSource
import com.glion.wol.data.db.datasource.DbDataSource
import com.glion.wol.data.mapper.toEntity
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : DbRepositoryImpl
 * Created by glion on 2025-09-02
 *
 * Description:
 * - 기기 내부의 데이터를 가져오는 LocalRepository 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Singleton
class LocalRepositoryImpl @Inject constructor(
    private val roomDs: DbDataSource,
    private val settingDs: SettingDataSource
) : LocalRepository {
    override suspend fun getAllDevice(): Flow<List<Device>> = flow {
        emit(roomDs.getAllDevice().map { it.toModel() })
    }.flowOn(Dispatchers.IO)

    override suspend fun changeMacAddr(id: Long, newMacAddr: String): Flow<Unit> = flow {
        roomDs.changeMacAddr(id, newMacAddr)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override suspend fun changeAlias(id: Long, newAlias: String): Flow<Unit> = flow {
        roomDs.changeAlias(id, newAlias)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override suspend fun changePowerStatus(macAddr: String, status: Boolean): Flow<Unit> = flow {
        roomDs.changePowerStatus(macAddr, status)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override suspend fun insertDevice(vararg devices: Device): Flow<Unit> = flow {
        roomDs.insertDevice(*devices.map { it.toEntity() }.toTypedArray())
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteDevice(device: Device): Flow<Boolean> = flow {
        val deleteCount = roomDs.deleteDevice(device.toEntity())
        if(deleteCount > 0) {
            emit(true)
        } else {
            throw Exception("Nothing to Delete")
        }

    }.flowOn(Dispatchers.IO)

    // dataSource 에서 이미 Flow 를 넘겨주기 때문에, repository 에서 다시 flow 를 만들 필요는 없음
    override val selectedIndex: Flow<Long>
        get() = settingDs.selectedIndex


    override suspend fun editSelectedIndex(idx: Long) {
        settingDs.editSelectIndex(idx)
    }

    override val token: Flow<String?>
        get() = settingDs.token

    override suspend fun setToken(token: String) {
        settingDs.setToken(token)
    }
}