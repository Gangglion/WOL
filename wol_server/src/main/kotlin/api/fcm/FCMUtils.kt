package com.glion.api.fcm

import com.glion.api.auth.AesCryptUtil
import com.glion.extension.toEncodeB64
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message

object FCMUtils {
    var fcmToken: String? = null
        private set

    fun setFcmToken(newToken: String) {
        println("get newToken :: $newToken")
        fcmToken = newToken
    }

    fun requestPush(mac: String, status: String): String {
        try {
            // 1. 전송할 MAC 주소 AES로 암호화
            val encrypted = AesCryptUtil.encryptAes(mac)
            // 2. 전송할 Data Payload 정의
            val dataPayload = mapOf(
                "encryptedValue" to encrypted.first.toEncodeB64(),
                "iv" to encrypted.second.toEncodeB64(),
                "status" to status
            )
            // 3. message 객체 생성
            val message = Message.builder()
                .putAllData(dataPayload)
                .setToken(fcmToken)
                .build()

            val response = FirebaseMessaging.getInstance().send(message)
            return response
        } catch(e: Exception) {
            e.printStackTrace()
            return "Error in requestPush"
        }
    }
}