package com.glion.wol.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.usecase.AddDeviceUseCase
import com.glion.wol.domain.usecase.ChangeDeviceInfoUseCase
import com.glion.wol.domain.usecase.GetAllDeviceUseCase
import com.glion.wol.domain.usecase.RemoveDeviceUseCase
import com.glion.wol.util.FlowResult
import com.glion.wol.util.deleteColon
import com.glion.wol.util.withColon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
    private val getAllDeviceUseCase: GetAllDeviceUseCase,
    private val changeDeviceInfoUseCase: ChangeDeviceInfoUseCase,
    private val addDeviceUseCase: AddDeviceUseCase,
    private val removeDeviceUseCase: RemoveDeviceUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState

    init {
        getAllDeviceList()
    }

    fun clickAddMode(status: Boolean) {
        val editDevice = if(status) null else _uiState.value.editDevice
        _uiState.update {
            it.copy(
                isAddMode = status,
                editDevice = editDevice,
                inputAlias = "",
                inputMac = ""
            )
        }
    }

    fun changeEditMode(editDevice: Device?) {
        _uiState.update {
            it.copy(
                isAddMode = false,
                editDevice = editDevice,
                inputMac = editDevice?.mac?.deleteColon() ?: "",
                inputAlias = editDevice?.alias ?: ""
            )
        }
    }

    fun inputMac(input: String) {
        if(input.length > 12) {
            showSnackbarMsg("Mac 주소는 12자 여야 합니다.")
        } else {
            _uiState.update {
                it.copy(inputMac = input)
            }
        }
    }

    fun inputAlias(input: String) {
        if(input.length > 10) {
            showSnackbarMsg("별명은 10글자를 넘을 수 없습니다.")
        } else {
            _uiState.update {
                it.copy(inputAlias = input)
            }
        }
    }

    private fun getAllDeviceList() {
        viewModelScope.launch {
            getAllDeviceUseCase.invoke().collect { getResult ->
                when(getResult) {
                    is FlowResult.Loading -> {

                    }
                    is FlowResult.Success -> {
                        _uiState.update {
                            it.copy(
                                deviceList = getResult.data,
                                isAddMode = false,
                                editDevice = null,
                                inputMac = "",
                                inputAlias = ""
                            )
                        }
                    }
                    is FlowResult.Error -> {
                        showSnackbarMsg(getResult.errorMsg)
                    }
                }
            }
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
                        getAllDeviceList()
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
            changeDeviceInfoUseCase.invoke(_uiState.value.editDevice!!, newDeviceWithColon).collect { updateResult ->
                when(updateResult) {
                    is FlowResult.Loading -> {

                    }
                    is FlowResult.Success -> {
                        if(updateResult.data) {
                            getAllDeviceList()
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
                            getAllDeviceList()
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