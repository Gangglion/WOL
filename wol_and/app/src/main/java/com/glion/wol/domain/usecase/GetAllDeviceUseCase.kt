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
 * File : GetAllDeviceUsecase
 * Created by glion on 2025-09-04
 *
 * Description:
 * - 모든 기기 리스트 가져오는 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class GetAllDeviceUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(): Flow<FlowResult<List<Device>>> {
        return localRepository.getAllDevice()
            .map<List<Device>, FlowResult<List<Device>>> {
                FlowResult.Success(it)
            }
            .onStart {
                emit(FlowResult.Loading)
            }
            .catch { e ->
                LogUtil.e("GetAllDevice has Error", e)
                emit(FlowResult.Error("", e.message ?: "UnKnown"))
            }
    }
}