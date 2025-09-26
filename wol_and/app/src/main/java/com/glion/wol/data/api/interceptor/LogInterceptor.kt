package com.glion.wol.data.api.interceptor

import com.glion.wol.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Project : WOL
 * File : LogInterceptor
 * Created by glion on 2025-09-25
 *
 * Description:
 * - 별도 관리 로그 Interceptor
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
val logInterceptor = HttpLoggingInterceptor(PrettyJsonLogger()).apply {
    level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}