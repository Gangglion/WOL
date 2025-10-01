package com.glion.wol.data.api.interceptor

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.internal.platform.Platform
import okhttp3.internal.platform.Platform.Companion.INFO
import okhttp3.internal.platform.Platform.Companion.WARN
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Project : WOL
 * File : PrettyJsonLogger
 * Created by glion on 2025-09-25
 *
 * Description:
 * - 네트워크 로그 정렬된 상태로 출력하기 위한 interceptor
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class PrettyJsonLogger : HttpLoggingInterceptor.Logger {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    override fun log(message: String) {
        val trimMessage = message.trim()

        if ((trimMessage.startsWith("{") && trimMessage.endsWith("}"))
            || (trimMessage.startsWith("[") && trimMessage.endsWith("]"))) {
            try {
                val prettyJson = gson.toJson(JsonParser.parseString(message))
                Platform.get().log(prettyJson, INFO,null)
            } catch (e: Exception) {
                Platform.get().log(message, WARN, e)
            }
        } else {
            Platform.get().log(message, INFO, null)
        }
    }
}