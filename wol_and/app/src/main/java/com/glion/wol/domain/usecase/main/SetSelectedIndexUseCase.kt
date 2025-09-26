package com.glion.wol.domain.usecase.main

import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    // editSelectedIndex 는 일회성 동작이므로, flow 빌더와 try-catch 를 사용하여 완료 시 Success emit, 예외 발생 시 catch 로 잡아 Error emit 하도록 구성
    operator fun invoke(index: Long) : Flow<FlowResult<Boolean>> = flow {
        try {
            localRepository.editSelectedIndex(index)
            emit(FlowResult.Success(true))
        } catch(e: Exception) {
            LogUtil.e("SetSelectedIndex has Error", e)
            emit(FlowResult.Error("", e.message ?: "UnKnown"))
        }
    }
}