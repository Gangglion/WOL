package com.glion.wol.domain.usecase.edit

import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.repository.DeviceRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Project : WOL
 * File : ChangeDeviceInfoUseCase
 * Created by glion on 2025-09-05
 *
 * Description:
 * - 기기 정보 변경
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class ChangeDeviceInfoUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    operator fun invoke(oldDevice: Device, newDevice: Device) : Flow<FlowResult<Boolean>> = flow {
        when {
            oldDevice.mac == newDevice.mac && oldDevice.alias == newDevice.alias -> { // 아무런 변경이 없을 때
                return@flow
            }

            oldDevice.mac == newDevice.mac && oldDevice.alias != newDevice.alias -> { // 별명만 변경되었을 때
                emit(FlowResult.Loading)
                try {
                    deviceRepository.changeAlias(newDevice.id, newDevice.alias)
                    emit(FlowResult.Success(true))
                } catch(e: Exception) {
                    LogUtil.e("changeAlias has Error", e)
                    emit(FlowResult.Error("", e.message ?: "UnKnown"))
                }
            }

            oldDevice.mac != newDevice.mac && oldDevice.alias == newDevice.alias -> { // 맥주소만 변경되었을 때
                emit(FlowResult.Loading)
                try {
                    deviceRepository.changeMacAddr(newDevice.id, newDevice.mac)
                    emit(FlowResult.Success(true))
                } catch(e: Exception) {
                    LogUtil.e("changeMacAddr has Error", e)
                    emit(FlowResult.Error("", e.message ?: "UnKnown"))
                }
            }

            else -> { // 둘다 변경되었을 때
                emit(FlowResult.Loading)
                try {
                    // 맥 주소 변경 및 별칭 변경 동시 진행
                    coroutineScope {
                        val macChangeJob = async { deviceRepository.changeMacAddr(newDevice.id, newDevice.mac) }
                        val aliasChangeJob = async { deviceRepository.changeAlias(newDevice.id, newDevice.alias) }

                        macChangeJob.await()
                        aliasChangeJob.await()
                    }
                    emit(FlowResult.Success(true))
                } catch(e: Exception) {
                    LogUtil.e("ChangeDeviceInfoUseCase has Error", e)
                    emit(FlowResult.Error("", e.message ?: "UnKnown"))
                }
            }
        }
    }
}