package com.glion.wol.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glion.wol.domain.model.Device
import com.glion.wol.domain.usecase.GetAllDeviceUseCase
import com.glion.wol.domain.usecase.GetDeviceUseCase
import com.glion.wol.domain.usecase.SetSelectedIndexUseCase
import com.glion.wol.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
data class TurnOnUiState(
    val deviceList: List<Device> = emptyList(),
    val selectedDevice: Device? = null,
    val userMsg: String? = null
)

@HiltViewModel
class TurnOnViewModel @Inject constructor(
    private val getAllDeviceUseCase: GetAllDeviceUseCase,
    private val getDeviceUseCase: GetDeviceUseCase,
    private val setSelectedIndexUseCase: SetSelectedIndexUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TurnOnUiState())
    val uiState : StateFlow<TurnOnUiState> = _uiState

    init {
        viewModelScope.launch {
            getDeviceUseCase.invoke().collect { deviceResult ->
                when(deviceResult) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(selectedDevice = deviceResult.data)
                        }
                    }
                    is Result.Error -> {

                    }
                }
            }
        }
    }

    fun getAllDevice() {
        viewModelScope.launch {
            getAllDeviceUseCase.invoke().collect { result ->
                when(result) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                deviceList = result.data
                            )
                        }
                    }
                    is Result.Error -> {

                    }
                }
            }
        }
    }

    fun setSelectedDevice(select: Device) {
        viewModelScope.launch {
            setSelectedIndexUseCase.invoke(select.id).collect { setResult ->
                when(setResult) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(selectedDevice = select)
                        }
                    }
                    is Result.Error -> {

                    }
                    is Result.Loading -> {  }
                }
            }
        }
    }

    fun powerOn() {

    }

    fun showSnackbarMsg(msg: String) {
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