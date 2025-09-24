package com.glion.wol.ui.splash

/**
 * Project : WOL
 * File : WolSplashUiState
 * Created by glion on 2025-09-24
 *
 * Description:
 * - 스플래시 화면 상태
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class WolSplashUiState(
    val goMain: Boolean = false,
    val errorMsg: String? = null
)
