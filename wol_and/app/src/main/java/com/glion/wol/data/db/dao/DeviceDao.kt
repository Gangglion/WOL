package com.glion.wol.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.glion.wol.data.db.entity.DeviceEntity
import kotlinx.coroutines.flow.Flow

/**
 * Project : WOL
 * File : DeviceDao
 * Created by glion on 2025-09-02
 *
 * Description:
 * - Room Device Table DAO
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Dao
interface DeviceDao {
    @Query("SELECT * FROM device")
    fun getAllDevice(): Flow<List<DeviceEntity>>

    @Query("SELECT alias FROM DEVICE WHERE mac_addr LIKE :macAddr LIMIT 1")
    suspend fun getAlias(macAddr: String) : String?

    @Query("UPDATE device SET mac_addr = :newMacAddr WHERE id LIKE :id")
    suspend fun changeMacAddr(id: Long, newMacAddr: String)

    @Query("UPDATE device SET alias = :newAlias WHERE id LIKE :id")
    suspend fun changeAlias(id: Long, newAlias: String)

    @Query("UPDATE device SET is_powerOn = :status WHERE mac_addr LIKE :macAddr")
    suspend fun changePowerStatus(macAddr: String, status: Boolean)

    @Insert
    suspend fun insertDevice(vararg deviceEntities: DeviceEntity)

    @Delete
    suspend fun deleteDevice(deviceEntity: DeviceEntity): Int // 삭제된 row 수 반환
}