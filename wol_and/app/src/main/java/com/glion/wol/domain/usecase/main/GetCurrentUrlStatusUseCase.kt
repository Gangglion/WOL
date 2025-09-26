package com.glion.wol.domain.usecase.main

import com.glion.wol.BuildConfig
import com.glion.wol.data.api.UrlProvider
import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Project : WOL
 * File : GetCurrentUrlStatus
 * Created by glion on 2025-09-25
 *
 * Description:
 * - 저장된 URL 을 가져와 외부인지 내부인지 반환
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class GetCurrentUrlStatusUseCase @Inject constructor(
    private val urlProvider: UrlProvider
) {
    operator fun invoke() : Flow<FlowResult<Boolean>> {
        return urlProvider.getUrlFlow
            .map { getUrl ->
                FlowResult.Success(BuildConfig.DDNS_IN == getUrl)
            }
            .catch { e ->
                LogUtil.e("GetCurrentUrlStatusUseCase has Error", e)
                FlowResult.Error("", e.message ?: "UnKnown")
            }
    }
}