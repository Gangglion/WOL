package com.glion

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.FileInputStream
import java.net.NetworkInterface
import java.util.*
import kotlin.system.exitProcess

fun main() {
    println("프로그램을 시작합니다...")

    try {
        // 1. 실행 시 내 MAC 주소 구하기
        val macAddress = getMacAddress()

        if (macAddress == null) {
            println("오류: MAC 주소를 찾을 수 없습니다.")
            exitProcess(1) // 오류 코드(1)와 함께 종료
        }

        println("MAC 주소를 찾았습니다: $macAddress")

        // 2. API 호출하기
        println("API 서버로 데이터를 전송합니다...")
        val success = sendMacAddressToServer(macAddress)

        if (success) {
            println("API 호출에 성공했습니다.")
        } else {
            println("오류: API 호출에 실패했습니다.")
            exitProcess(1) // 오류 코드(1)와 함께 종료
        }

    } catch (e: Exception) {
        println("알 수 없는 오류가 발생했습니다: ${e.message}")
        exitProcess(1) // 오류 코드(1)와 함께 종료
    }

    // 3. 호출 완료 후 종료
    println("모든 작업이 완료되어 프로그램을 종료합니다. ✅")
    exitProcess(0) // 정상 종료
}

/**
 * 기기의 첫 번째 활성화된 네트워크 인터페이스의 MAC 주소를 가져옵니다.
 */
private fun getMacAddress(): String? {
    try {
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            if (!intf.isUp || intf.hardwareAddress == null) continue // 비활성화 또는 MAC 주소 없는 인터페이스는 건너뛰기

            val macBytes = intf.hardwareAddress
            val macString = StringBuilder()
            for (i in macBytes.indices) {
                macString.append(String.format("%02X%s", macBytes[i], if (i < macBytes.size - 1) ":" else ""))
            }
            return macString.toString()
        }
    } catch (ex: Exception) {
        // 오류 처리
    }
    return null
}

/**
 * MAC 주소를 API 서버로 전송합니다.
 * @return 성공 시 true, 실패 시 false
 */
private fun sendMacAddressToServer(macAddress: String): Boolean {
    return try {
        val client = OkHttpClient()
        val properties = Properties()
        properties.load(FileInputStream("local.properties"))
        val url = properties.getProperty("API_URL")

        val json = """{"mac": "$macAddress", "status" : "Off"}"""
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            response.isSuccessful // HTTP 상태 코드가 2xx 이면 true 반환
        }
    } catch (e: Exception) {
        println("API 전송 중 오류 발생: ${e.message}")
        false
    }
}