package com.glion.wol.domain.usecase

import com.glion.wol.domain.model.Device
import com.glion.wol.domain.repository.LocalRepository
import javax.inject.Inject

/**
 * Project : WOL
 * File : RemoveDeviceUseCase
 * Created by glion on 2025-09-05
 *
 * Description:
 * - 기기 삭제 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class RemoveDeviceUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(device: Device) = localRepository.deleteDevice(device)
}