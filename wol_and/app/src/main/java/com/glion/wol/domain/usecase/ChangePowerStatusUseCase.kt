package com.glion.wol.domain.usecase

import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Project : WOL
 * File : ChangePowerStatusUseCase
 * Created by glion on 2025-09-05
 *
 * Description:
 * - 전원 상태 변경하는 UseCase
 *
 * Copyright @2025 UBIPLUS. All rights reserved
 */
class ChangePowerStatusUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(macAddr: String, status: Boolean) : Flow<FlowResult<Boolean>> {
        return localRepository.changePowerStatus(macAddr, status)
            .map<Unit, FlowResult<Boolean>> {
                FlowResult.Success(true)
            }
            .onStart {
                emit(FlowResult.Loading)
            }
            .catch { e ->
                LogUtil.e("ChangePowerStatus has Error", e)
                emit(FlowResult.Error("", e.message ?: "UnKnown"))
            }
    }
}