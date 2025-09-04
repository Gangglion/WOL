package com.glion.wol.domain.usecase

import com.glion.wol.domain.repository.LocalRepository
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
    suspend operator fun invoke() = localRepository.getAllDevice()
}