package com.glion.api.fcm

import com.glion.Config
import com.glion.api.auth.AesCryptUtil
import com.glion.extension.toEncodeB64
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import java.io.File

object FCMUtils {
    var fcmToken: String? = null
        private set

    fun init() {
        if(isExistTokenFile())
            fcmToken = loadFcmToken()
    }

    /**
     * 해당 경로에 토큰 파일 존재하는지 확인
     */
    fun isExistTokenFile() : Boolean {
        val file = File(Config.fcmKeyPath)
        return file.exists()
    }

    /**
     * FCM 토큰 파일에서 값 가져와 리턴
     */
    fun loadFcmToken(): String {
        val file = File(Config.fcmKeyPath)
        return file.readText(Charsets.UTF_8)
    }

    /**
     * 새로운 토큰 저장 후 파일에 쓰기
     */
    fun setFcmToken(newToken: String) {
        fcmToken = newToken
        val file = File(Config.fcmKeyPath)
        val parentDir = file.parentFile
        if(parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs()
        }
        file.writeText(newToken, Charsets.UTF_8)
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
            val token = fcmToken ?: loadFcmToken()
            // 3. message 객체 생성
            val message = Message.builder()
                .putAllData(dataPayload)
                .setToken(token)
                .build()

            val response = FirebaseMessaging.getInstance().send(message)
            return response
        } catch(e: Exception) {
            e.printStackTrace()
            return "Error in requestPush"
        }
    }
}