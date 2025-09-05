package com.glion.wol.ui.edit

import com.glion.wol.domain.model.Device

/**
 * Project : WOL
 * File : EditUiState
 * Created by glion on 2025-09-05
 *
 * Description:
 * - 수정 화면 UiState
 * @param deviceList 기기 리스트
 * @param isAddMode 추가중 여부
 * @param editDevice 수정중인 Device
 * @param inputMac 입력한 맥 주소
 * @param inputAlias 입력한 별명
 * @param userMsg 스낵바에 표시될 내용
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class EditUiState(
    val deviceList: List<Device> = emptyList(),
    val isAddMode: Boolean = false,
    val editDevice: Device? = null,
    val inputMac: String = "",
    val inputAlias: String = "",
    val userMsg: String? = null
)
