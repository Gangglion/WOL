package com.glion.wol.domain.usecase

import com.glion.wol.domain.model.Device
import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Project : WOL
 * File : GetDeviceUseCase
 * Created by glion on 2025-09-04
 *
 * Description:
 * - 아이디와 일치하는 기기 가져오기 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class GetDeviceUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke() : Flow<Result<Device>> {
        return localRepository.selectedIndex
            .flatMapLatest { indexResult ->
                when(indexResult) {
                    is Result.Success -> localRepository.findTargetDevice(indexResult.data)
                    is Result.Error -> flowOf(Result.Error(indexResult.errorCode, indexResult.errorMsg))
                    else -> flowOf(Result.Loading)
                }
            }
    }
}