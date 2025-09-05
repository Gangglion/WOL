package com.glion.wol.data.repository

import com.glion.wol.data.datastore.datasource.SettingDataSource
import com.glion.wol.data.db.datasource.DbDataSource
import com.glion.wol.data.mapper.toEntity
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.Device
import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.FlowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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
class LocalRepositoryImpl @Inject constructor(
    private val roomDs: DbDataSource,
    private val settingDs: SettingDataSource
) : LocalRepository {
    override suspend fun getAllDevice(): Flow<FlowResult<List<Device>>> = flow {
        emit(FlowResult.Loading)
        try {
            val response = roomDs.getAllDevice().map { it.toModel() }
            emit(FlowResult.Success(response))
        } catch(e: Exception) {
            e.printStackTrace()
            emit(FlowResult.Error("", e.message ?: "LocalRepositoryImpl's getAllDevice Exception"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun changeMacAddr(id: Long, newMacAddr: String): Flow<FlowResult<Boolean>> = flow {
        emit(FlowResult.Loading)
        try {
            roomDs.changeMacAddr(id, newMacAddr)
            emit(FlowResult.Success(true))
        } catch(e: Exception) {
            emit(FlowResult.Error("", e.message ?: "LocalRepositoryImpl's changeMacAddr Exception"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun changeAlias(id: Long, newAlias: String): Flow<FlowResult<Boolean>> = flow {
        emit(FlowResult.Loading)
        try {
            roomDs.changeAlias(id, newAlias)
            emit(FlowResult.Success(true))
        } catch(e: Exception) {
            emit(FlowResult.Error("", e.message ?: "LocalRepositoryImpl's changeAlias Exception"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun changePowerStatus(macAddr: String, status: Boolean): Flow<FlowResult<Boolean>> = flow {
        emit(FlowResult.Loading)
        try {
            roomDs.changePowerStatus(macAddr, status)
            emit(FlowResult.Success(true))
        } catch(e: Exception) {
            emit(FlowResult.Error("", e.message ?: "LocalRepositoryImpl's changePowerStatus Exception"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun insertDevice(vararg devices: Device): Flow<FlowResult<Boolean>> = flow {
        emit(FlowResult.Loading)
        try {
            roomDs.insertDevice(*devices.map { it.toEntity() }.toTypedArray())
            emit(FlowResult.Success(true))
        } catch(e: Exception) {
            emit(FlowResult.Error("", e.message ?: "LocalRepositoryImpl's insertDevice Exception"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteDevice(device: Device): Flow<FlowResult<Boolean>> = flow {
        emit(FlowResult.Loading)
        try {
            val deleteCount = roomDs.deleteDevice(device.toEntity())
            if(deleteCount > 0) {
                emit(FlowResult.Success(true))
            } else {
                emit(FlowResult.Error("", "Nothing Delete"))
            }
        } catch(e: Exception) {
            emit(FlowResult.Error("", e.message ?: "LocalRepositoryImpl's deleteDevice Exception"))
        }
    }.flowOn(Dispatchers.IO)

    // dataSource 에서 이미 Flow 를 넘겨주기 때문에, repository 에서 다시 flow 를 만들 필요는 없음
    override val selectedIndex: Flow<FlowResult<Long>>
        get() = settingDs.selectedIndex
            .map<Long, FlowResult<Long>> { FlowResult.Success(it) }
            .catch { e ->
                e.printStackTrace()
                emit(FlowResult.Error("", e.message ?: "unknown error in LocalRepositoryImpl's selectedIndex"))
            }


    override suspend fun editSelectedIndex(idx: Long): Flow<FlowResult<Boolean>> = flow {
        try {
            settingDs.editSelectIndex(idx)
            emit(FlowResult.Success(true))
        } catch(e: Exception) {
            emit(FlowResult.Error("", e.message ?: "LocalRepositoryImpl's editSelectedIndex Exception"))
        }
    }.flowOn(Dispatchers.IO)
}