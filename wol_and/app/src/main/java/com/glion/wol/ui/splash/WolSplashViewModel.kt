package com.glion.wol.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glion.wol.domain.usecase.splash.InitializeUseCase
import com.glion.wol.util.FlowResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
    private val initializeUseCase: InitializeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WolSplashUiState())
    val uiState: StateFlow<WolSplashUiState> = _uiState

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            initializeUseCase().collect { result ->
                when(result) {
                    is FlowResult.Success -> {
                        _uiState.update {
                            it.copy(goMain = true)
                        }
                    }
                    is FlowResult.Error -> {
                        setSnackbarMsg(result.errorMsg)
                    }
                    is FlowResult.Loading -> {

                    }
                }
            }
        }
    }

    fun setSnackbarMsg(msg: String) {
        _uiState.update {
            it.copy(errorMsg = msg)
        }
    }

    fun clearSnackbarMsg() {
        _uiState.update {
            it.copy(errorMsg = null)
        }
    }
}