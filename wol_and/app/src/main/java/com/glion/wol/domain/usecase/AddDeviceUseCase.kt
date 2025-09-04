package com.glion.wol.domain.usecase

import com.glion.wol.domain.model.Device
import com.glion.wol.domain.repository.LocalRepository
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
    suspend operator fun invoke(device: Device) = localRepository.insertDevice(device)
}