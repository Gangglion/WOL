package com.glion.wol.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.usecase.common.GetAllDeviceUseCase
import com.glion.wol.domain.usecase.edit.AddDeviceUseCase
import com.glion.wol.domain.usecase.edit.ChangeDeviceInfoUseCase
import com.glion.wol.domain.usecase.edit.RemoveDeviceUseCase
import com.glion.wol.util.FlowResult
import com.glion.wol.util.deleteColon
import com.glion.wol.util.withColon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
 * File : EditScreenViewModel
 * Created by glion on 2025-03-12
 *
 * Description:
 * - EditScreen ViewModel
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@HiltViewModel
class EditScreenViewModel @Inject constructor(
    getAllDeviceUseCase: GetAllDeviceUseCase,
    private val changeDeviceInfoUseCase: ChangeDeviceInfoUseCase,
    private val addDeviceUseCase: AddDeviceUseCase,
    private val removeDeviceUseCase: RemoveDeviceUseCase
) : ViewModel() {
    private val _snackbarFlow = MutableSharedFlow<String>()
    val snackbarFlow : SharedFlow<String> = _snackbarFlow

    // UI 상태 관리를 위한 별도의 StateFlow
    private val _isAddMode = MutableStateFlow(false)
    private val _editDevice = MutableStateFlow<Device?>(null)
    private val _inputMac = MutableStateFlow("")
    private val _inputAlias = MutableStateFlow("")

    // getAllDeviceUseCase 의 결과가 에러라면, 이를 관찰하여 메시지를 스낵바로 노출
    private val _allDeviceFlow = getAllDeviceUseCase()
        .onEach { result ->
            if(result is FlowResult.Error) {
                showSnackbarMsg(result.errorMsg)
            }
        }
    // 최종 UI 상태를 관리
    val uiState: StateFlow<EditUiState> = combine(
        _allDeviceFlow,
        _isAddMode,
        _editDevice,
        _inputMac,
        _inputAlias
    ) { allDevice, isAddMode, editDevice, inputMac, inputAlias ->
        when(allDevice) {
            is FlowResult.Success -> {
                EditUiState(
                    isLoading = false,
                    deviceList = allDevice.data,
                    isAddMode = isAddMode,
                    editDevice = editDevice,
                    inputMac = inputMac,
                    inputAlias = inputAlias
                )
            }
            is FlowResult.Error -> {
                EditUiState(isLoading = false)
            }
            is FlowResult.Loading -> {
                EditUiState(isLoading = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L), // 리소스 낭비를 줄이기 위해 화면을 벗어나면 5초 이후에 Flow 구독 중지
        initialValue = EditUiState(isLoading = true)
    )

    fun clickAddMode(status: Boolean) {
        _isAddMode.value = status
        if(status) _editDevice.value = null
        // 입력 필드 초기화
        _inputMac.value = ""
        _inputAlias.value = ""
    }

    fun changeEditMode(editDevice: Device?) {
        _isAddMode.value = false
        _editDevice.value = editDevice
        _inputMac.value = editDevice?.mac?.deleteColon() ?: ""
        _inputAlias.value = editDevice?.alias ?: ""
    }

    fun inputMac(input: String) {
        if(input.length > 12) {
            showSnackbarMsg("Mac 주소는 12자 여야 합니다.")
        } else {
            _inputMac.value = input
        }
    }

    fun inputAlias(input: String) {
        if(input.length > 10) {
            showSnackbarMsg("별명은 10글자를 넘을 수 없습니다.")
        } else {
            _inputAlias.value = input
        }
    }

    fun addDevice(device: Device) {
        if(device.mac.length < 12) {
            showSnackbarMsg("Mac 주소는 12자여야 합니다")
            return
        }
        // 맥주소 콜론 붙여서 변환
        val withColonDevice = device.copy(mac = device.mac.withColon())
        viewModelScope.launch {
            addDeviceUseCase.invoke(withColonDevice).collect { addResult ->
                when(addResult) {
                    is FlowResult.Loading -> {

                    }
                    is FlowResult.Success -> {
                        screenState()
                        showSnackbarMsg("기기를 추가하였습니다.")
                    }
                    is FlowResult.Error -> {
                        showSnackbarMsg(addResult.errorMsg)
                    }
                }
            }
        }
    }

    fun changeDeviceInfo(newDevice: Device) {
        val newDeviceWithColon = newDevice.copy(mac = newDevice.mac.withColon())
        viewModelScope.launch {
            changeDeviceInfoUseCase.invoke(_editDevice.value!!, newDeviceWithColon).collect { updateResult ->
                when(updateResult) {
                    is FlowResult.Loading -> {

                    }
                    is FlowResult.Success -> {
                        if(updateResult.data) {
                            screenState()
                            showSnackbarMsg("변경이 완료되었습니다.")
                        }
                    }
                    is FlowResult.Error -> {
                        showSnackbarMsg(updateResult.errorMsg)
                    }
                }
            }
        }
    }

    fun removeDevice(device: Device) {
        viewModelScope.launch {
            removeDeviceUseCase.invoke(device).collect { removeResult ->
                when(removeResult) {
                    is FlowResult.Loading -> {

                    }
                    is FlowResult.Success -> {
                        if(removeResult.data) {
                            screenState()
                            showSnackbarMsg("기기를 삭제했습니다.")
                        }
                    }
                    is FlowResult.Error -> {
                        showSnackbarMsg(removeResult.errorMsg)
                    }
                }
            }
        }
    }

    private fun showSnackbarMsg(msg: String) {
        viewModelScope.launch {
            _snackbarFlow.emit(msg)
        }
    }

    /**
     * 화면 상태 초기화
     */
    private fun screenState() {
        _isAddMode.value = false
        _editDevice.value = null
        _inputMac.value = ""
        _inputAlias.value = ""
    }
}