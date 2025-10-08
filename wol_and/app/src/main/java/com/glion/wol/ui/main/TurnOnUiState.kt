package com.glion.wol.ui.main

import com.glion.wol.domain.model.local.Device

/**
 * Project : WOL
 * File : TurnOnUiState
 * Created by glion on 2025-09-05
 *
 * Description:
 * - 메인 화면 UiState
 * @param isLoading 로딩 상태, 기본값 false
 * @param deviceList 기기 리스트
 * @param selectedDevice 선택된 기기, 기본값 null
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class TurnOnUiState(
    val isLoading: Boolean = false,
    val deviceList: List<Device> = emptyList(),
    val selectedDevice: Device? = null
)
