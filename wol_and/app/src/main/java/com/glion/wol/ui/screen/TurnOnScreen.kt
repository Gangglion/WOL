package com.glion.wol.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glion.wol.R

/**
 * Project : WOL
 * File : TurnOnScreen
 * Created by Ganggion on 2025-03-12
 *
 * Description:
 * - 전원을 켤 기기와 전원 버튼 존재하는 화면
 *
 * Copyright @2025 Ganggion. All rights reserved
 */

@Composable
fun TurnOnScreen(
    modifier: Modifier = Modifier,
    viewModel: TurnOnViewModel = viewModel(),
    sbHost: SnackbarHostState? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.setDefaultDevice()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultDeviceText(
            modifier = Modifier.padding(vertical = 36.dp),
            uiState = uiState
        )
        IconButton(
            onClick = {
                // TODO : 다이어로그 Open
                viewModel.powerOn()
            },
            modifier = Modifier.size(128.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_power),
                contentDescription = null,
                tint = if(!uiState.isPowerOn) Color.Red else Color.Green,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    uiState.userMsg?.let { message ->
        LaunchedEffect(Unit) {
            sbHost?.showSnackbar(message)
            viewModel.clearSnackbarMsg()
        }
    }
}

@Composable
fun DefaultDeviceText(
    modifier: Modifier = Modifier,
    uiState: TurnOnUiState? = null
) {
    if(uiState == null) {
        Text(
            modifier = modifier,
            text = "지정된 기기가 없습니다. 확인해주세요",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    } else {
        Column(
            modifier = modifier
        ) {
            Text(
                text = uiState.alias,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = uiState.macAddr,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = uiState.ddns,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTurnOnScreen() {
    TurnOnScreen()
}