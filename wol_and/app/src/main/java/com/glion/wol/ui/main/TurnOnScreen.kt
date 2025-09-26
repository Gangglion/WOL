package com.glion.wol.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glion.wol.R
import com.glion.wol.domain.model.local.Device
import com.glion.wol.ui.appbar.TopAppBar
import kotlinx.coroutines.launch

/**
 * Project : WOL
 * File : TurnOnScreen
 * Created by glion on 2025-03-12
 *
 * Description:
 * - 전원을 켤 기기와 전원 버튼 존재하는 화면
 *
 * Copyright @2025 Ganggion. All rights reserved
 */

@Composable
fun TurnOnScreen(
    modifier: Modifier = Modifier,
    viewModel: TurnOnViewModel = hiltViewModel(),
    sbHost: SnackbarHostState,
    goSetting: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    SettingDrawer(
        drawerState = drawerState,
        deviceList = uiState.deviceList,
        onClickDevice = {
            scope.launch {
                drawerState.close()
            }
            viewModel.setSelectedIndex(it)
        },
        goSetting = {
            goSetting()
            scope.launch {
                drawerState.close()
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    openDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if(isClosed) open() else close()
                            }
                        }
                    },
                    drawerState = drawerState
                )
            }
        ) { innerPadding ->
            TurnOnScreenContent(
                modifier = Modifier.padding(innerPadding),
                uiState = uiState,
                clickPowerOn = { viewModel.powerOn() },
                changeUrlStatus = { value -> viewModel.changeUrlStatus(value) }
            )
        }
    }

    // 스낵바 메시지
    LaunchedEffect(Unit) {
        viewModel.snackbarFlow.collect { msg ->
            sbHost.showSnackbar(msg)
        }
    }
}

@Composable
fun TurnOnScreenContent(
    modifier: Modifier = Modifier,
    uiState: TurnOnUiState,
    clickPowerOn: () -> Unit,
    changeUrlStatus: (Boolean) -> Unit
) {
    Box {
        with(uiState){
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selectedDevice == null) {
                    Text(
                        modifier = Modifier.padding(vertical = 36.dp, horizontal = 16.dp),
                        text = "지정된 기기가 없습니다.\n확인해주세요",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = selectedDevice.alias,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = selectedDevice.mac,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    IconButton(
                        onClick = clickPowerOn,
                        modifier = Modifier.size(128.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_power),
                            contentDescription = null,
                            tint = if (!selectedDevice.isPowerOn) Color.Red else Color.Green,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Switch(
                checked = isInternalMode,
                onCheckedChange = changeUrlStatus,
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTurnOnScreen() {
    TurnOnScreenContent(
        uiState = TurnOnUiState(),
        clickPowerOn = {},
        changeUrlStatus = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTurnOnScreenDevice() {
    TurnOnScreenContent(
        uiState = TurnOnUiState(
            selectedDevice = Device(
                mac = "AA:AA:AA:AA:AA:AA",
                alias = "테스트",
                isPowerOn = false
            )
        ),
        clickPowerOn = {},
        changeUrlStatus = {}
    )
}