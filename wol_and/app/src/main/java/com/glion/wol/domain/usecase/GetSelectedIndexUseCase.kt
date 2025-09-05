package com.glion.wol.domain.usecase

import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.FlowResult
import kotlinx.coroutines.flow.Flow
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
    operator fun invoke() : Flow<FlowResult<Long>> = localRepository.selectedIndex
}