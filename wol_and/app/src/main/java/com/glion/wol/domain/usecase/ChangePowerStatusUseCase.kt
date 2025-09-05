package com.glion.wol.domain.usecase

import com.glion.wol.domain.repository.LocalRepository
import jakarta.inject.Inject

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
    suspend operator fun invoke(macAddr: String, status: Boolean) = localRepository.changePowerStatus(macAddr, status)
}