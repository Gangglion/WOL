package com.glion.wol.ui.edit

import androidx.lifecycle.ViewModel
import com.glion.wol.domain.usecase.GetAllDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Project : WOL
 * File : EditScreenViewModel
 * Created by glion on 2025-03-12
 *
 * Description:
 * - 추후 기입
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@HiltViewModel
class EditScreenViewModel @Inject constructor(
    private val getAllDeviceUseCase: GetAllDeviceUseCase
) : ViewModel() {

}