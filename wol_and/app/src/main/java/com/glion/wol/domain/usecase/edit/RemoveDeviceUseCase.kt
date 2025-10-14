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
 * File : RemoveDeviceUseCase
 * Created by glion on 2025-09-05
 *
 * Description:
 * - 기기 삭제 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class RemoveDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    operator fun invoke(device: Device) : Flow<FlowResult<Boolean>> = flow {
        emit(FlowResult.Loading)
        try {
            val deleteResult = deviceRepository.deleteDevice(device)
            emit(FlowResult.Success(deleteResult))
        } catch(e: Exception) {
            LogUtil.e("RemoveDevice has Error", e)
            emit(FlowResult.Error("", e.message ?: "UnKnown"))
        }
    }
}