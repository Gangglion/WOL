package com.glion.wol.domain.model

/**
 * Project : WOL
 * File : Device
 * Created by glion on 2025-09-02
 *
 * Description:
 * - Device Model
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class Device(
    val id: Long = 0,
    val mac: String = "",
    val alias: String = "",
    val isPowerOn: Boolean = false
)
