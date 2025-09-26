package com.glion.wol.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    initializeUseCase: InitializeUseCase
) : ViewModel() {
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent

    // 화면 이동 상태
    private val _navigateEvent = MutableSharedFlow<Boolean>()
    val navigateEvent: SharedFlow<Boolean> = _navigateEvent

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
                    if(result.data) _navigateEvent.emit(true)
                    WolSplashUiState(isLoading = false)
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


    fun setSnackbarMsg(msg: String) {
        viewModelScope.launch {
            _snackbarEvent.emit(msg)
        }
    }
}