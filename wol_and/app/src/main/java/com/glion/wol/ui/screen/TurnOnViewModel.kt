package com.glion.wol.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glion.wol.BuildConfig
import com.glion.wol.util.WakeOnLan
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Project : WOL
 * File : TurnonViewModel
 * Created by Gangglion on 2025-03-12
 *
 * Description:
 * - TurnOnScreen ViewModel
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
data class TurnOnUiState(
    val alias: String = "",
    val macAddr: String = "",
    val ddns: String = "",
    val isPowerOn: Boolean = false,
    val userMsg: String? = null
)

class TurnOnViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TurnOnUiState())
    val uiState : StateFlow<TurnOnUiState> = _uiState

    fun setDefaultDevice() {
        _uiState.update {
            it.copy(
                alias = "Desktop",
                macAddr = BuildConfig.DEFAULT_MAC,
                ddns = BuildConfig.DDNS
            )
        }
    }

    fun powerOn() {
        viewModelScope.launch {
            if(_uiState.value.macAddr.isNotEmpty() && _uiState.value.ddns.isNotEmpty()) {
                async {
                    WakeOnLan.sendMagicPacket(
                        macAddr = _uiState.value.macAddr,
                        ddns = _uiState.value.ddns
                    )
                }.await()

                // 전송 완료 이후
                _uiState.update {
                    it.copy(
                        isPowerOn = true,
                        userMsg = "${_uiState.value.alias} 에 매직패킷을 전송하였습니다."
                    )
                }
            } else {
                showSnackbarMsg("MAC 주소와 DDNS를 다시 확인해주세요.")
            }
        }
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