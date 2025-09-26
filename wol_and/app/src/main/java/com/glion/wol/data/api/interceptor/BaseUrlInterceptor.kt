package com.glion.wol.data.api.interceptor

import com.glion.wol.data.api.UrlProvider
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Project : WOL
 * File : BaseUrlInterceptor
 * Created by glion on 2025-09-25
 *
 * Description:
 * - 선택된 URL 에 따라 변경하는 Interceptor
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class BaseUrlInterceptor @Inject constructor(
    private val urlProvider: UrlProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val currentUrl = urlProvider.getUrl().toHttpUrl()

        val newUrl = request.url.newBuilder()
            .scheme(currentUrl.scheme)
            .host(currentUrl.host)
            .port(currentUrl.port)
            .build()

        request = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(request)
    }
}