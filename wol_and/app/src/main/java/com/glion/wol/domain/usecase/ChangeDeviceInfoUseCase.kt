package com.glion.wol.domain.usecase

import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
                    .map<Unit, FlowResult<Boolean>> {
                        FlowResult.Success(true)
                    }
                    .onStart { emit(FlowResult.Loading) }
                    .catch { e ->
                        LogUtil.e("changeAlias has Error", e)
                        emit(FlowResult.Error("", e.message ?: "UnKnown"))
                    }
            }

            oldDevice.mac != newDevice.mac && oldDevice.alias == newDevice.alias -> { // 맥주소만 변경되었을 때
                localRepository.changeMacAddr(newDevice.id, newDevice.mac)
                    .map<Unit, FlowResult<Boolean>> {
                        FlowResult.Success(true)
                    }
                    .onStart {
                        emit(FlowResult.Loading)
                    }
                    .catch { e ->
                        LogUtil.e("changeMacAddr has Error", e)
                        emit(FlowResult.Error("", e.message ?: "UnKnown"))
                    }
            }

            else -> { // 둘다 변경되었을 때
                val macFlow: Flow<FlowResult<Boolean>> = localRepository.changeMacAddr(newDevice.id, newDevice.mac)
                    .map<Unit, FlowResult<Boolean>> {
                        FlowResult.Success(true)
                    }
                    .catch { e ->
                        LogUtil.e("changeMacAddr has Error", e)
                        emit(FlowResult.Error("", e.message ?: "UnKnown"))
                    }
                val aliasFlow: Flow<FlowResult<Boolean>> = localRepository.changeAlias(newDevice.id, newDevice.alias)
                    .map<Unit, FlowResult<Boolean>> {
                        FlowResult.Success(true)
                    }
                    .catch { e ->
                        LogUtil.e("changeAlias has Error", e)
                        emit(FlowResult.Error("", e.message ?: "UnKnown"))
                    }

                combine(macFlow, aliasFlow) { macRes, aliasRes ->
                    if(macRes is FlowResult.Success && aliasRes is FlowResult.Success) {
                        FlowResult.Success(true)
                    } else {
                        val errorMessage = mutableListOf<String>()

                        if(macRes is FlowResult.Error) {
                            errorMessage.add("changeMacAddr has Error : ${macRes.errorMsg}")
                        }
                        if(aliasRes is FlowResult.Error) {
                            errorMessage.add("changeAlias has Error : ${aliasRes.errorMsg}")
                        }
                        FlowResult.Error("", errorMessage.joinToString("\n"))
                    }
                }.onStart {
                    emit(FlowResult.Loading)
                }
            }
        }
    }
}