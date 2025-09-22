package com.glion.wol.domain.usecase

import com.glion.wol.domain.repository.RemoteRepository
import javax.inject.Inject

class ExchangeKeyUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {
    suspend operator fun invoke() {
        remoteRepository.exchangeKey()
    }
}