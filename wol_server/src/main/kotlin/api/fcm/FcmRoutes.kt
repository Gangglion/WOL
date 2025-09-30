package com.glion.api.fcm

import com.glion.api.auth.AesCryptUtil
import com.glion.api.data.RequestEncryptedCommon
import com.glion.api.data.RespondCommon
import com.glion.api.fcm.data.RequestSendPush
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun Route.fcmRoutes() {
    route("/fcm") {
        authenticate("auth-jwt") {
            post("/sendPushToken") {
                val request = call.receive<RequestEncryptedCommon>()
                val encFcmToken = request.encryptedDataBase64.decodeBase64Bytes()
                val iv = request.ivBase64.decodeBase64Bytes()
                val getToken = AesCryptUtil.decryptAes(encFcmToken, iv)
                FCMUtils.setFcmToken(getToken)
                call.respond(RespondCommon(result = true, message = "Success"))
            }
        }

        post("/sendPush") {
            val request = call.receive<RequestSendPush>()
            val pushResponse = FCMUtils.requestPush(request.mac, request.status)
            call.respond(RespondCommon(result = true, message = pushResponse))
        }
    }
}
