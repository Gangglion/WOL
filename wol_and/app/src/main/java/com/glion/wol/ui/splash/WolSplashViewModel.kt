package com.glion.wol.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glion.crypto_module.RSAUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            // 1. RSA 키 가져오기(없으면 생성)
            RSAUtils.getOrCreateRSAKeyPair()
            // 2. 생성 완료 후 AES 키 존재 확인 -> 없으면 키 교환
            withContext(Dispatchers.IO) {

            }
        }
    }
}