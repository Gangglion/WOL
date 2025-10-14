package com.glion

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.FileInputStream
import java.net.NetworkInterface
import java.util.*
import java.util.Locale.getDefault
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
        // 현재 실행 중인 운영체제의 이름을 가져옵니다 (소문자로 변환).
        val osName = System.getProperty("os.name").lowercase(getDefault())

        // 1. OS가 Linux인 경우 (NAS 환경)
        if (osName.contains("linux")) {
            println("Linux 환경 감지. 'eth0' 인터페이스를 찾습니다.")
            val eth0 = NetworkInterface.getByName("eth0")
            if (eth0 != null && eth0.hardwareAddress != null) {
                // 'eth0'의 MAC 주소를 포맷에 맞게 변환하여 반환합니다.
                return eth0.hardwareAddress.joinToString(":") { String.format("%02X", it) }
            }
        }

        // 2. Windows 및 기타 OS인 경우 (기존 로직)
        println("Windows 또는 기타 환경 감지. 첫 번째 활성 인터페이스를 찾습니다.")
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            // 활성화 상태이고, MAC 주소가 있는 첫 번째 인터페이스를 찾아 반환합니다.
            if (intf.isUp && intf.hardwareAddress != null) {
                return intf.hardwareAddress.joinToString(":") { String.format("%02X", it) }
            }
        }

    } catch (ex: Exception) {
        println("MAC 주소 조회 중 오류가 발생했습니다: ${ex.message}")
    }

    // 어떤 경우에도 MAC 주소를 찾지 못하면 null을 반환합니다.
    return null
}

/**
 * MAC 주소를 API 서버로 전송합니다.
 * @return 성공 시 true, 실패 시 false
 */
private fun sendMacAddressToServer(macAddress: String): Boolean {
    return try {
        val properties = Properties()
        val classLoader = Thread.currentThread().contextClassLoader
        val inputStream = classLoader.getResourceAsStream("local.properties")

        inputStream.use { stream ->
            if(stream == null) {
                println("오류 : JAR 파일 내에서 local.properties 를 찾을 수 없습니다.")
                return false
            }
            properties.load(stream)
        }
        val url = properties.getProperty("API_URL")
        if(url.isNullOrEmpty()) {
            println("오류 : local.properties 파일에 API_URL 이 비어있습니다.")
            return false
        }

        val client = OkHttpClient()
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