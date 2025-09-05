package com.glion.wol.domain.repository

import com.glion.wol.domain.model.Device
import com.glion.wol.util.FlowResult
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
    suspend fun getAllDevice() : Flow<FlowResult<List<Device>>>

    suspend fun changeMacAddr(id: Long, newMacAddr: String) : Flow<FlowResult<Boolean>>

    suspend fun changeAlias(id: Long, newAlias: String) : Flow<FlowResult<Boolean>>

    suspend fun changePowerStatus(macAddr: String, status: Boolean) : Flow<FlowResult<Boolean>>

    suspend fun insertDevice(vararg devices: Device) : Flow<FlowResult<Boolean>>

    suspend fun deleteDevice(device: Device) : Flow<FlowResult<Boolean>>

    val selectedIndex: Flow<FlowResult<Long>>

    suspend fun editSelectedIndex(idx: Long): Flow<FlowResult<Boolean>>
}