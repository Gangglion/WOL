package com.glion.wol.ui.main

import com.glion.wol.domain.model.local.Device

/**
 * Project : WOL
 * File : TurnOnUiState
 * Created by glion on 2025-09-05
 *
 * Description:
 * - 메인 화면 UiState
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class TurnOnUiState(
    val deviceList: List<Device> = emptyList(),
    val selectedDevice: Device? = null,
    val userMsg: String? = null
)
