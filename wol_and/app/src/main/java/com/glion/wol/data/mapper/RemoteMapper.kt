package com.glion.wol.data.mapper

import com.glion.wol.data.api.data.ResponseCommon
import com.glion.wol.domain.model.remote.CommonResult

/**
 * Project : WOL
 * File : RemoteMapper
 * Created by glion on 2025-09-22
 *
 * Description:
 * - 추후 기입
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

fun ResponseCommon.toModel() = CommonResult(
    result = this.result,
    message = this.message
)
