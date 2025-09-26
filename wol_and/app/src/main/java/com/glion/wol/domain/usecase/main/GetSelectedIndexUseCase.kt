package com.glion.wol.domain.usecase.main

import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Project : WOL
 * File : GetMainDataUseCase
 * Created by glion on 2025-09-25
 *
 * Description:
 * - SelectedIndex 가져오는 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class GetSelectedIndexUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke() : Flow<FlowResult<Long>> {
        return localRepository.selectedIndex
            .map {
                FlowResult.Success(it)
            }
            .catch { e ->
                LogUtil.e("GetSelectedIndexUseCase has Error", e)
                FlowResult.Error("", e.message ?: "UnKnown")
            }
    }
}