package com.glion.wol.domain.repository

import com.glion.wol.domain.model.local.Device
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
    fun getAllDevice() : Flow<List<Device>>

    suspend fun changeMacAddr(id: Long, newMacAddr: String) : Flow<Unit>

    suspend fun changeAlias(id: Long, newAlias: String) : Flow<Unit>

    suspend fun insertDevice(vararg devices: Device) : Flow<Unit>

    suspend fun deleteDevice(device: Device) : Flow<Boolean>

    val selectedIndex: Flow<Long>

    /**
     * 선택한 index 수정 - 일회성 동작으로 반환값 없음
     */
    suspend fun editSelectedIndex(idx: Long)
}