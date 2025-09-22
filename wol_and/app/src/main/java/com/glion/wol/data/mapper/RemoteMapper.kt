package com.glion.wol.data.mapper

import com.glion.wol.data.api.data.ResponseWolStart
import com.glion.wol.domain.model.remote.WolResult

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

fun ResponseWolStart.toModel() = WolResult(
    result = this.result,
    message = this.message
)
