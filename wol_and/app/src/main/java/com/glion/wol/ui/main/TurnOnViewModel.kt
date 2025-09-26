package com.glion.wol.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glion.wol.domain.usecase.common.GetAllDeviceUseCase
import com.glion.wol.domain.usecase.main.GetCurrentUrlStatusUseCase
import com.glion.wol.domain.usecase.main.GetSelectedIndexUseCase
import com.glion.wol.domain.usecase.main.PowerOnUseCase
import com.glion.wol.domain.usecase.main.SetCurrentUrlStatusUseCase
import com.glion.wol.domain.usecase.main.SetSelectedIndexUseCase
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project : WOL
 * File : TurnonViewModel
 * Created by glion on 2025-03-12
 *
 * Description:
 * - TurnOnScreen ViewModel
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@HiltViewModel
class TurnOnViewModel @Inject constructor(
    getCurrentUrlStatusUseCase: GetCurrentUrlStatusUseCase,
    getAllDeviceUseCase: GetAllDeviceUseCase,
    getSelectedIndexUseCase: GetSelectedIndexUseCase,
    private val setCurrentUrlStatusUseCase: SetCurrentUrlStatusUseCase,
    private val setSelectedIndexUseCase: SetSelectedIndexUseCase,
    private val powerOnUseCase: PowerOnUseCase
) : ViewModel() {
    private val _snackbarFlow = MutableSharedFlow<String>()
    val snackbarFlow: SharedFlow<String> = _snackbarFlow

    // 모든 기기 정보 가져오는 Flow - 에러 관찰하여 Snackbar 띄워줌
    private val _getAllDeviceFlow = getAllDeviceUseCase()
        .onEach { result ->
            if(result is FlowResult.Error) {
                showSnackbarMsg(result.errorMsg)
            }
        }
    private val _getCurrentUrlStatusFlow = getCurrentUrlStatusUseCase()
        .onEach { result ->
            if(result is FlowResult.Error) {
                showSnackbarMsg(result.errorMsg)
            }
        }

    private val _getSelectedIndexFlow = getSelectedIndexUseCase()
        .onEach { result ->
            if(result is FlowResult.Error) {
                showSnackbarMsg(result.errorMsg)
            }
        }

    val uiState : StateFlow<TurnOnUiState> = combine(
        _getAllDeviceFlow,
        _getSelectedIndexFlow,
        _getCurrentUrlStatusFlow,
    ) { allDeviceResult, selectedIndexResult, currentUrlModeResult ->
        val internalMode = (currentUrlModeResult as? FlowResult.Success)?.data ?: false
        val selectedIndex = (selectedIndexResult as? FlowResult.Success)?.data ?: 0L

        when(allDeviceResult) {
            is FlowResult.Success -> {
                TurnOnUiState(
                    isLoading = false,
                    isInternalMode = internalMode,
                    deviceList = allDeviceResult.data,
                    selectedDevice = allDeviceResult.data.find { device -> device.id == selectedIndex }
                )
            }
            is FlowResult.Error -> {
                TurnOnUiState(isLoading = false)
            }
            is FlowResult.Loading -> {
                TurnOnUiState(isLoading = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000L),
        initialValue = TurnOnUiState(isLoading = true)
    )

    fun setSelectedIndex(selectId: Long) {
        viewModelScope.launch {
            setSelectedIndexUseCase.invoke(selectId).collect { setResult ->
                when(setResult) {
                    is FlowResult.Error -> {
                        LogUtil.e(setResult.errorMsg)
                        showSnackbarMsg(setResult.errorMsg)
                    }
                    else -> {  }
                }
            }
        }
    }

    fun powerOn() {
        if(uiState.value.selectedDevice!!.isPowerOn) {
            showSnackbarMsg("이미 전원이 켜져 있습니다")
            return
        }
        viewModelScope.launch {
            with(uiState.value) {
                if(this != null) {
                    powerOnUseCase(selectedDevice!!.mac).collect { result ->
                        when(result) {
                            is FlowResult.Success -> {
                                showSnackbarMsg(msg = "${selectedDevice.alias} 의 전원을 켜는 중입니다.")
                            }
                            is FlowResult.Error -> {
                                showSnackbarMsg(msg = result.errorMsg)
                            }
                            is FlowResult.Loading -> {

                            }
                        }
                    }
                }
            }
        }
    }

    fun changeUrlStatus(status: Boolean) {
        viewModelScope.launch {
            setCurrentUrlStatusUseCase(status).collect { result ->
                when(result) {
                    is FlowResult.Success -> {
                        showSnackbarMsg(msg = if(status) "내부망 전환" else "외부망 전환")
                    }
                    is FlowResult.Error -> {
                        showSnackbarMsg(msg = result.errorMsg)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showSnackbarMsg(msg: String) {
        viewModelScope.launch {
            _snackbarFlow.emit(msg)
        }
    }
}