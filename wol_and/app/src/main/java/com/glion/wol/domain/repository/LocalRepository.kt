package com.glion.wol.domain.repository

import com.glion.wol.domain.model.Device
import com.glion.wol.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Project : WOL
 * File : DbRepository
 * Created by glion on 2025-09-02
 *
 * Description:
 * - Local Repository 정의
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface LocalRepository {
    suspend fun getAllDevice() : Flow<Result<List<Device>>>

    suspend fun findTargetDevice(id: Long) : Flow<Result<Device>>

    suspend fun changeMacAddr(id: Long, newMacAddr: String) : Flow<Result<Boolean>>

    suspend fun changeAlias(id: Long, newAlias: String) : Flow<Result<Boolean>>

    suspend fun changePowerStatus(macAddr: String, status: Boolean) : Flow<Result<Boolean>>

    suspend fun insertDevice(vararg devices: Device) : Flow<Result<Boolean>>

    suspend fun deleteDevice(device: Device) : Flow<Result<Boolean>>

    val selectedIndex: Flow<Result<Long>>

    suspend fun editSelectedIndex(idx: Long): Flow<Result<Boolean>>
}