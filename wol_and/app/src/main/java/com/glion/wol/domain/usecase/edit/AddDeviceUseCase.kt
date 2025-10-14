package com.glion.wol.domain.usecase.edit

import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.repository.DeviceRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Project : WOL
 * File : AddDeviceUseCase
 * Created by glion on 2025-09-04
 *
 * Description:
 * - 기기 저장 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class AddDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    operator fun invoke(device: Device) : Flow<FlowResult<Boolean>> = flow {
        emit(FlowResult.Loading)
        try {
            deviceRepository.insertDevice(device)
            emit(FlowResult.Success(true))
        } catch(e: Exception) {
            LogUtil.e("AddDevice has Error", e)
            emit(FlowResult.Error("", e.message ?: "AddDevice has Error"))
        }
    }
}