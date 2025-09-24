package com.glion.wol.domain.usecase

import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(device: Device) : Flow<FlowResult<Boolean>> {
        return localRepository.insertDevice(device)
            .map<Unit, FlowResult<Boolean>> { _ ->
                FlowResult.Success(true)
            }
            .onStart {
                emit(FlowResult.Loading)
            }
            .catch { e ->
                LogUtil.e("AddDevice has Error", e)
                emit(FlowResult.Error("", e.message ?: "AddDevice has Error"))
            }
    }
}