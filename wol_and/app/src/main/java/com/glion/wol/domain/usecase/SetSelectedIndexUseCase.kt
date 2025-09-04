package com.glion.wol.domain.usecase

import com.glion.wol.domain.repository.LocalRepository
import javax.inject.Inject

/**
 * Project : WOL
 * File : SetSelectedIndexUseCase
 * Created by glion on 2025-09-04
 *
 * Description:
 * - 선택된 기기의 Index 저장하는 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class SetSelectedIndexUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(index: Long) = localRepository.editSelectedIndex(index)
}