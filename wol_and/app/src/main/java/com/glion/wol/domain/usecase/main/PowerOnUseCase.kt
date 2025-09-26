package com.glion.wol.domain.usecase.main

import com.glion.wol.domain.repository.CryptoRepository
import com.glion.wol.domain.repository.RemoteRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import com.glion.wol.util.b64Encode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Project : WOL
 * File : PowerOnUseCase
 * Created by glion on 2025-09-24
 *
 * Description:
 * - 기기 전원 켜기 UseCase
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class PowerOnUseCase @Inject constructor(
    private val cryptoRepository: CryptoRepository,
    private val remoteRepository: RemoteRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(mac: String) : Flow<FlowResult<Boolean>> {
        return cryptoRepository.encryptAES(mac)
            .flatMapConcat { encryptResult ->
                remoteRepository.startDevice(
                    mac = encryptResult.first.b64Encode(),
                    iv = encryptResult.second.b64Encode()
                )
            }
            .map { wolResult ->
                if(wolResult.result) {
                    FlowResult.Success(true)
                } else {
                    FlowResult.Error("", wolResult.message)
                }
            }
            .onStart {
                emit(FlowResult.Loading)
            }
            .catch { e ->
                LogUtil.e("PowerOnUseCase has Error", e)
                emit(FlowResult.Error("", e.message ?: "UnKnown"))
            }
    }
}