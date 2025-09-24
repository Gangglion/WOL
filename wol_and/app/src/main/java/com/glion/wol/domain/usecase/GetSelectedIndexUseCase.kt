package com.glion.wol.domain.usecase

import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Project : WOL
 * File : GetSelectedIndexUseCase
 * Created by glion on 2025-09-05
 *
 * Description:
 * - 선택한 기기 index 반환
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class GetSelectedIndexUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke(): Flow<FlowResult<Long>> {
        return localRepository.selectedIndex
            .map<Long, FlowResult<Long>> {
                FlowResult.Success(it)
            }
            .catch { e ->
                LogUtil.e("GetSelectedIndex has Error", e)
                emit(FlowResult.Error("", e.message ?: "UnKnown"))
            }
    }
}