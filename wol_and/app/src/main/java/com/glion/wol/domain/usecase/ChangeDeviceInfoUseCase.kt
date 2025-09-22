package com.glion.wol.domain.usecase

import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.FlowResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
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
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(oldDevice: Device, newDevice: Device) : Flow<FlowResult<Boolean>> {
        return when {
            oldDevice.mac == newDevice.mac && oldDevice.alias == newDevice.alias -> { // 아무런 변경이 없을 때
                emptyFlow()
            }
            oldDevice.mac == newDevice.mac && oldDevice.alias != newDevice.alias -> { // 별명만 변경되었을 때
                localRepository.changeAlias(newDevice.id, newDevice.alias)
            }
            oldDevice.mac != newDevice.mac && oldDevice.alias == newDevice.alias -> { // 맥주소만 변경되었을 때
                localRepository.changeMacAddr(newDevice.id, newDevice.mac)
            }
            else -> { // 둘다 변경되었을 때
                val macFlow = localRepository.changeMacAddr(newDevice.id, newDevice.mac)
                val aliasFlow = localRepository.changeAlias(newDevice.id, newDevice.alias)

                combine(macFlow, aliasFlow) { macRes, aliasRes ->
                    if(macRes is FlowResult.Success && aliasRes is FlowResult.Success) {
                        FlowResult.Success(true)
                    } else {
                        val error = macRes as? FlowResult.Error ?: aliasRes as? FlowResult.Error
                        FlowResult.Error(
                            error?.errorCode ?: "",
                            error?.errorMsg ?: "맥 주소 및 별명 변경 중 오류 발생"
                        )
                    }
                }
            }
        }
    }
}