package com.glion.wol.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glion.wol.domain.model.Device
import com.glion.wol.domain.usecase.ChangePowerStatusUseCase
import com.glion.wol.domain.usecase.GetAllDeviceUseCase
import com.glion.wol.domain.usecase.GetSelectedIndexUseCase
import com.glion.wol.domain.usecase.SetSelectedIndexUseCase
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
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
    private val getAllDeviceUseCase: GetAllDeviceUseCase,
    private val getSelectedIndexUseCase: GetSelectedIndexUseCase,
    private val setSelectedIndexUseCase: SetSelectedIndexUseCase,
    private val changePowerStatusUseCase: ChangePowerStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TurnOnUiState())
    val uiState : StateFlow<TurnOnUiState> = _uiState

    fun getAllDeviceAndSelectedDevice() {
        viewModelScope.launch {
            val getIndexFlow = getSelectedIndexUseCase.invoke()
            val getAllDeviceFlow = getAllDeviceUseCase.invoke()
            combine(getIndexFlow, getAllDeviceFlow) { indexRes, allRes ->
                when {
                    indexRes is FlowResult.Loading || allRes is FlowResult.Loading -> {
                        // 로딩중
                    }
                    indexRes is FlowResult.Success && allRes is FlowResult.Success -> {
                        _uiState.update {
                            it.copy(
                                deviceList = allRes.data,
                                selectedDevice = allRes.data.find { device -> device.id == indexRes.data }
                            )
                        }
                    }
                    else -> {
                        val error = indexRes as? FlowResult.Error ?: allRes as? FlowResult.Error
                        LogUtil.e(error?.errorMsg ?: "저장한 기기 index 와 전체 기기 가져오는 과정에서 오류 발생")
                        _uiState.update {
                            it.copy(userMsg = error?.errorMsg ?: "저장한 기기 index 와 전체 기기 가져오는 과정에서 오류 발생")
                        }
                    }
                }
            }.collect {  }
        }
    }

    fun setSelectedDevice(select: Device) {
        viewModelScope.launch {
            setSelectedIndexUseCase.invoke(select.id).collect { setResult ->
                when(setResult) {
                    is FlowResult.Success -> {
                        _uiState.update {
                            it.copy(selectedDevice = select)
                        }
                    }
                    is FlowResult.Error -> {
                        LogUtil.e(setResult.errorMsg)
                        _uiState.update {
                            it.copy(userMsg = setResult.errorMsg)
                        }
                    }
                    is FlowResult.Loading -> {  }
                }
            }
        }
    }

    fun powerOn() {
        if(_uiState.value.selectedDevice!!.isPowerOn) {
            showSnackbarMsg("이미 전원이 켜져 있습니다")
            return
        }
        viewModelScope.launch {
            with(_uiState.value) {
                if(selectedDevice != null) {
                    // TODO : 전원 켜기 UseCase 실행 후, 그 결과가 Success 라면
                    _uiState.update {
                        it.copy(userMsg = "${selectedDevice.alias} 의 전원을 켜는 중입니다.")
                    }
                }
            }
        }
    }

    private fun showSnackbarMsg(msg: String) {
        _uiState.update {
            it.copy(userMsg = msg)
        }
    }

    fun clearSnackbarMsg() {
        _uiState.update {
            it.copy(userMsg = null)
        }
    }
}