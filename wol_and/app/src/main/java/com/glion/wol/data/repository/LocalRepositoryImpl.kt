package com.glion.wol.data.repository

import com.glion.wol.data.datastore.datasource.SettingDataSource
import com.glion.wol.data.db.datasource.DbDataSource
import com.glion.wol.data.mapper.toEntity
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    override fun getAllDevice(): Flow<List<Device>> {
        return roomDs.getAllDevice().map { deviceList -> // Flow 가공
            deviceList.map { it.toModel() } // Flow 내의 데이터 Model로 변환
        }
    }

    override suspend fun changeMacAddr(id: Long, newMacAddr: String) {
        roomDs.changeMacAddr(id, newMacAddr)
    }

    override suspend fun changeAlias(id: Long, newAlias: String) {
        roomDs.changeAlias(id, newAlias)
    }

    override suspend fun insertDevice(vararg device: Device) {
        roomDs.insertDevice(*device.map { it.toEntity() }.toTypedArray())
    }

    override suspend fun deleteDevice(device: Device) : Boolean{
        val deleteCount = roomDs.deleteDevice(device.toEntity())
        if(deleteCount > 0) {
            return true
        } else {
            throw Exception("Nothing to Delete")
        }
    }

    // dataSource 에서 이미 Flow 를 넘겨주기 때문에, repository 에서 다시 flow 를 만들 필요는 없음
    override val selectedIndex: Flow<Long>
        get() = settingDs.selectedIndex


    override suspend fun editSelectedIndex(idx: Long) {
        settingDs.editSelectIndex(idx)
    }
}