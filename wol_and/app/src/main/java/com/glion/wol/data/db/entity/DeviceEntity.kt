package com.glion.wol.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project : WOL
 * File : Device
 * Created by glion on 2025-09-02
 *
 * Description:
 * - Device Room Entity
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Entity(tableName = "device")
data class DeviceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "mac_addr") val macAddr: String,
    @ColumnInfo(name = "alias") val alias: String,
    @ColumnInfo(name = "is_powerOn") val isPowerOn: Boolean
)