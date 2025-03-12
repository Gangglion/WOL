package com.glion.wol.ui.drawer

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Project : WOL
 * File : SettingDrawer
 * Created by Gangglion on 2025-03-12
 *
 * Description:
 * - 기기 설정 화면 Drawer
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

@Composable
fun SettingDrawer(
    drawerState: DrawerState,
    viewModel: SettingDrawerViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val uiState = viewModel
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Test"
                )
                Text(
                    text = "Test"
                )
                Text(
                    text = "Test"
                )
                Text(
                    text = "Test"
                )
                Text(
                    text = "Test"
                )
                Text(
                    text = "Test"
                )
                Text(
                    text = "Test"
                )
                Text(
                    text = "Test"
                )
                Text(
                    text = "Test"
                )
            }
        },
        gesturesEnabled = true,
        content = content
    )
}