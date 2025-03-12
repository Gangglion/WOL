package com.glion.wol.ui.drawer

import androidx.lifecycle.ViewModel
import com.glion.wol.data.db.Device

/**
 * Project : WOL
 * File : SettingDrawerViewModel
 * Created by shhan on 2025-03-12
 *
 * Description:
 * - 추후 기입
 *
 * Copyright @2025 UBIPLUS. All rights reserved
 */

data class DrawerUiState(
    val savedDevices: List<Device>
)
class SettingDrawerViewModel : ViewModel() {
}