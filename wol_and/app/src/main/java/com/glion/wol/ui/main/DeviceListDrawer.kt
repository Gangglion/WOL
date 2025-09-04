package com.glion.wol.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glion.wol.R
import com.glion.wol.domain.model.Device

/**
 * Project : WOL
 * File : SettingDrawer
 * Created by glion on 2025-03-12
 *
 * Description:
 * - 기기 설정 화면 Drawer
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

@Composable
fun SettingDrawer(
    drawerState: DrawerState,
    deviceList: List<Device>,
    onClickDevice: (Device) -> Unit,
    goSetting: () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DeviceDrawerContent(
                deviceList = deviceList,
                onClickDevice = onClickDevice,
                goSetting = goSetting
            )
        },
        gesturesEnabled = true,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        content()
    }
}

@Composable
fun DeviceDrawerContent(
    deviceList: List<Device>,
    onClickDevice: (Device) -> Unit,
    goSetting: () -> Unit,
) {
    ModalDrawerSheet {
        if(deviceList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "저장된 기기가 없습니다",
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(deviceList) { device ->
                    DeviceItem(
                        device = device,
                        itemClick = { onClickDevice(device) }
                    )
                }
            }
        }

        IconButton(
            onClick = goSetting,
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = null
            )
        }
    }
}

@Composable
fun DeviceItem(
    device: Device,
    itemClick: () -> Unit
) {
    NavigationDrawerItem(
        modifier = Modifier.padding(vertical = 4.dp),
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = device.alias,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = device.mac,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.ic_power),
                    tint = if(device.isPowerOn) Color.Green else Color.Red,
                    contentDescription = null
                )
            }
        },
        selected = false,
        onClick = itemClick
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyDeviceDrawerContent() {
    DeviceDrawerContent(
        deviceList = emptyList(),
        onClickDevice = {},
        goSetting = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDeviceDrawerContent() {
    DeviceDrawerContent(
        deviceList = listOf(
            Device(
                alias = "Test",
                mac = "AA:AA:AA:AA:AA:AA",
                isPowerOn = false
            ),
            Device(
                alias = "Test2",
                mac = "BB:BB:BB:BB:BB:BB",
                isPowerOn = true
            ),
            Device(
                alias = "Test3",
                mac = "CC:CC:CC:CC:CC:CC",
                isPowerOn = false
            ),
            Device(
                alias = "Test4",
                mac = "DD:DD:DD:DD:DD:DD",
                isPowerOn = true
            ),
            Device(
                alias = "Test5",
                mac = "EE:EE:EE:EE:EE:EE",
                isPowerOn = false
            ),
        ),
        onClickDevice = {},
        goSetting = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDeviceItem() {
    DeviceItem(
        device = Device(
            alias = "Test",
            mac = "AA:AA:AA:AA:AA:AA",
            isPowerOn = false
        ),
        itemClick = {}
    )
}