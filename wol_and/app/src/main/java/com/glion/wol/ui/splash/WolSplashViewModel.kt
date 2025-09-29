package com.glion.wol.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glion.wol.domain.usecase.common.TokenSyncUseCase
import com.glion.wol.domain.usecase.splash.InitializeUseCase
import com.glion.wol.util.FlowResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project : WOL
 * File : SplashViewModel
 * Created by glion on 2025-09-22
 *
 * Description:
 * - 추후 기입
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@HiltViewModel
class WolSplashViewModel @Inject constructor(
    initializeUseCase: InitializeUseCase,
    private val tokenSyncUseCase: TokenSyncUseCase
) : ViewModel() {
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent

    private val _initializeFlow = initializeUseCase()
        .onEach { result ->
            if(result is FlowResult.Error) {
                setSnackbarMsg(result.errorMsg)
            }
        }

    val uiState: StateFlow<WolSplashUiState> = _initializeFlow
        .map { result ->
            when(result) {
                is FlowResult.Success -> {
                    if(result.data) {
                        tokenSyncUseCase.startSyncToken()
                        WolSplashUiState(isLoading = true, isInitialize = true)
                    } else {
                        WolSplashUiState(isLoading = true)
                    }
                }
                else -> {
                    WolSplashUiState(isLoading = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500L),
            initialValue = WolSplashUiState(isLoading = true)
        )

    /**
     * 권한이 거부되었을때
     */
    fun onNotificationPermissionDenied() {
        setSnackbarMsg("알림 권한을 허용해주어야 합니다.")
    }

    fun setSnackbarMsg(msg: String) {
        viewModelScope.launch {
            _snackbarEvent.emit(msg)
        }
    }
}