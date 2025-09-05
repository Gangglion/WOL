package com.glion.wol.ui.edit

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glion.wol.R
import com.glion.wol.domain.model.Device

/**
 * Project : WOL
 * File : EditScreen
 * Created by glion on 2025-03-12
 *
 * Description:
 * - 수정 화면
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    viewModel: EditScreenViewModel = hiltViewModel(),
    sbHost: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsState()

    EditScreenContent(
        modifier = Modifier.fillMaxSize(),
        onMacValueChanged = { viewModel.inputMac(it) },
        onAliasValueChanged = { viewModel.inputAlias(it) },
        onAddClick = { viewModel.clickAddMode(true) },
        onAddCancel = { viewModel.clickAddMode(false) },
        onAddComplete = { viewModel.addDevice(it) },
        onEditLongClick = { viewModel.changeEditMode(it) },
        onEditCancel = { viewModel.changeEditMode(null) },
        onEditComplete = { viewModel.changeDeviceInfo(it) },
        onRemoveItem = { viewModel.removeDevice(it) },
        uiState = uiState
    )

    uiState.userMsg?.let { message ->
        LaunchedEffect(Unit) {
            sbHost.showSnackbar(message)
            viewModel.clearSnackbarMsg()
        }
    }
}

@Composable
fun EditScreenContent(
    modifier: Modifier = Modifier,
    onMacValueChanged: (String) -> Unit,
    onAliasValueChanged: (String) -> Unit,
    onAddClick: () -> Unit,
    onAddCancel: () -> Unit,
    onAddComplete: (Device) -> Unit,
    onEditLongClick: (Device) -> Unit,
    onEditCancel: () -> Unit,
    onEditComplete: (Device) -> Unit,
    onRemoveItem: (Device) -> Unit,
    uiState: EditUiState
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.padding(top = 64.dp)
            ) {
                if(uiState.isAddMode) {
                    AddItem(
                        uiState = uiState,
                        onMacValueChanged = onMacValueChanged,
                        onAliasValueChanged = onAliasValueChanged,
                        onAddCancel = onAddCancel,
                        onAddComplete = onAddComplete
                    )
                } else {
                    IconButton(
                        onClick = onAddClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }

        items(
            items = uiState.deviceList
        ) { device ->
            // 수정중이거나 추가중이 아니며, 전원이 꺼져있는 기기만 삭제 가능
            val canSwipe = uiState.editDevice == null && !device.isPowerOn && !uiState.isAddMode
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if(it == SwipeToDismissBoxValue.StartToEnd && canSwipe) {
                        onRemoveItem(device)
                        true
                    } else {
                        false
                    }
                }
            )
            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = canSwipe,
                enableDismissFromEndToStart = false, // 오른쪽에서 왼쪽 스와이프는 아예 차단
                backgroundContent = {
                    val color by animateColorAsState(
                        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) MaterialTheme.colorScheme.error else Color.Transparent
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(color)
                            .border(
                                width = 1.dp,
                                color = color,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Icon",
                            tint = Color.White
                        )
                    }
                }
            ) {
                DeviceItem(
                    device = device,
                    uiState = uiState,
                    onMacValueChanged = onMacValueChanged,
                    onAliasValueChanged = onAliasValueChanged,
                    onEditLongClick = onEditLongClick,
                    onEditCancel = onEditCancel,
                    onEditComplete = onEditComplete
                )
            }
        }
    }
}

@Composable
fun AddItem(
    modifier: Modifier = Modifier,
    uiState: EditUiState,
    onMacValueChanged: (String) -> Unit,
    onAliasValueChanged: (String) -> Unit,
    onAddCancel: () -> Unit,
    onAddComplete: (Device) -> Unit
) {
    Box(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            DeviceInfoRow(
                title = "MAC",
                info = uiState.inputMac,
                isEdit = true,
                modifier = Modifier.padding(bottom = 4.dp),
                onValueChanged = onMacValueChanged
            )
            DeviceInfoRow(
                title = "Alias",
                info = uiState.inputAlias,
                isEdit = true,
                onValueChanged = onAliasValueChanged
            )
            ButtonGroup(
                onClickCancel = onAddCancel,
                onClickComplete = {
                    with(uiState) {
                        onAddComplete(
                            Device(
                                mac = inputMac,
                                alias = inputAlias,
                                isPowerOn = false
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun DeviceItem(
    modifier: Modifier = Modifier,
    device: Device,
    uiState: EditUiState,
    onMacValueChanged: (String) -> Unit,
    onAliasValueChanged: (String) -> Unit,
    onEditLongClick: (Device) -> Unit,
    onEditCancel: () -> Unit,
    onEditComplete: (Device) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val isEdit = device.id == uiState.editDevice?.id
    Box(
        modifier = modifier
            .padding(vertical = 4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onEditLongClick(device)
                }
            )
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 2.dp,
                color = if(isEdit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            DeviceInfoRow(
                title = "MAC",
                info = if(isEdit) uiState.inputMac else device.mac,
                isEdit = isEdit,
                modifier = Modifier.padding(bottom = 4.dp),
                onValueChanged = onMacValueChanged
            )
            DeviceInfoRow(
                title = "Alias",
                info = if(isEdit) uiState.inputAlias else device.alias,
                isEdit = isEdit,
                onValueChanged = onAliasValueChanged
            )
            if(isEdit) {
                ButtonGroup(
                    onClickCancel = onEditCancel,
                    onClickComplete = {
                        onEditComplete(
                            Device(
                                id = device.id,
                                mac = uiState.inputMac,
                                alias = uiState.inputAlias,
                                isPowerOn = device.isPowerOn
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun DeviceInfoRow(
    modifier: Modifier = Modifier,
    title: String,
    info: String,
    isEdit: Boolean,
    onValueChanged: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier.weight(1f),
            text = title,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            modifier = modifier.weight(3f),
            value = info,
            onValueChange = onValueChanged,
            enabled = isEdit
        )
    }
}

@Composable
fun ButtonGroup(
    onClickCancel: () -> Unit,
    onClickComplete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button (
            onClick = onClickCancel,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(
                text = "취소",
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(
            modifier = Modifier.padding(4.dp)
        )
        Button (
            onClick = onClickComplete,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "확인",
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEditScreenContent_Empty() {
    EditScreenContent(
        uiState = EditUiState(),
        onMacValueChanged = {},
        onAliasValueChanged = {},
        onAddClick = {},
        onAddCancel = {},
        onAddComplete = {},
        onEditLongClick = {},
        onEditCancel = {},
        onEditComplete = {},
        onRemoveItem = {}
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewEditScreenContent() {
    EditScreenContent(
        uiState = EditUiState(
            deviceList = listOf(
                Device(
                    mac = "AA:BB:CC:DD:EE:FF",
                    alias = "테스트 기기",
                    isPowerOn = false
                ),
                Device(
                    mac = "AA:BB:CC:DD:EE:FF",
                    alias = "테스트 기기2",
                    isPowerOn = false
                ),
                Device(
                    mac = "AA:BB:CC:DD:EE:FF",
                    alias = "테스트 기기3",
                    isPowerOn = true
                )
            )
        ),
        onMacValueChanged = {},
        onAliasValueChanged = {},
        onAddClick = {},
        onAddCancel = {},
        onAddComplete = {},
        onEditLongClick = {},
        onEditCancel = {},
        onEditComplete = {},
        onRemoveItem = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddItem() {
    AddItem(
        uiState = EditUiState(),
        onMacValueChanged = {},
        onAliasValueChanged = {},
        onAddCancel = {},
        onAddComplete = {}
    )
}