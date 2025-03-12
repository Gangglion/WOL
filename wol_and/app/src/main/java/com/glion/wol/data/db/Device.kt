package com.glion.wol.data.db

/**
 * Project : WOL
 * File : Device
 * Created by shhan on 2025-03-12
 *
 * Description:
 * - 추후 기입
 *
 * Copyright @2025 UBIPLUS. All rights reserved
 */
data class Device(
    val alias: String,
    val macAddr: String,
    val ddns: String,
    val isSelected: Boolean,
    val isPowerOn: Boolean
)