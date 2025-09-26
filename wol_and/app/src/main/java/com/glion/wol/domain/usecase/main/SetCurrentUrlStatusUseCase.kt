package com.glion.wol.domain.usecase.main

import com.glion.wol.data.api.UrlProvider
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Project : WOL
 * File : SetCurrentUrlStatusUseCase
 * Created by glion on 2025-09-25
 *
 * Description:
 * - URL 변경
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class SetCurrentUrlStatusUseCase @Inject constructor(
    private val urlProvider: UrlProvider
) {
    operator fun invoke(status: Boolean) : Flow<FlowResult<Unit>> = flow{
        try {
            urlProvider.setUrl(status)
            emit(FlowResult.Success(Unit))
        } catch(e: Exception) {
            LogUtil.d("SetCurrentUrlStatusUseCase has Error", e)
            emit(FlowResult.Error("", e.message ?: "UnKnown"))
        }
    }
}