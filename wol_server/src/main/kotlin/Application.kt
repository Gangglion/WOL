package com.glion

import com.glion.api.auth.AesCryptUtil
import com.glion.api.fcm.FCMUtils
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureJWT()
    configureMonitoring()
    configureSerialization()
    configureRouting()
    configureFirebase()
    // AES 키 유틸 초기화 - 키 값 가져옴
    AesCryptUtil.init()
    // FCM 유틸 초기화 - 존재하는 토큰 가져옴
    FCMUtils.init()
}