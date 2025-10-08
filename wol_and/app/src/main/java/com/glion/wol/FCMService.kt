package com.glion.wol

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.glion.wol.domain.repository.CryptoRepository
import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.util.LogUtil
import com.glion.wol.util.b64DecodeByteArray
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project : WOL
 * File : FCMService
 * Created by glion on 2025-09-29
 *
 * Description:
 * - Firebase Messaging Service
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var localRepository: LocalRepository
    @Inject
    lateinit var cryptoRepository: CryptoRepository

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    // 새로운 토큰이 생성됬을떄
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        LogUtil.d("onNewToken :: $newToken")
        scope.launch {
            try {
                // 1. 새로운 토큰 저장
                localRepository.saveFcmToken(newToken)
            } catch(e: Exception) {
                LogUtil.e("onNewToken has Error", e)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // check if message contains a data payload
        if(message.data.isNotEmpty()) {
            scope.launch {
                val encryptedValue = message.data["encryptedValue"]?.b64DecodeByteArray() ?: return@launch
                val iv = message.data["iv"]?.b64DecodeByteArray() ?: return@launch
                val status = message.data["status"].run { this == "On" }
                try {
                    // 1. 맥주소 복호화하여 가져오기
                    val mac = cryptoRepository.decryptedMac(encryptedValue, iv)
                    // 2. 맥주소의 PowerStatus 변경
                    localRepository.changePowerStatus(mac, status)
                    // 3. 맥 주소의 별칭 가져옴
                    val alias = localRepository.getAlias(mac)
                    // 4. Push 띄워줌
                    if(alias != null) {
                        sendNotification(
                            if(status) {
                                ContextCompat.getString(this@FCMService, R.string.device_on).format(alias)
                            } else {
                                ContextCompat.getString(this@FCMService, R.string.device_off).format(alias)
                            }
                        )
                    }
                } catch(e: IllegalArgumentException) {
                    // 앱의 프로세스가 죽어있어 복호화가 실패한 경우 - 수동으로 키 로드하여 복호화 진행해주어야 함
                    // 1. RSA 키 로드
                    val rsaKey = cryptoRepository
                    // 2. AES 키 로드 + RSA 로 복호화

                    // 3. 맥 주소 복호화하여 가져옴

                    // 4. 복호화한 맥 주소의 별칭 가져옴

                    // 5. 푸시 보냄
                } catch(e: Exception) {
                    // status 에 따라 push 만 띄워줌
                    LogUtil.e("Error in onMessageReceived", e)
                    sendNotification(
                        if(status) {
                            ContextCompat.getString(this@FCMService, R.string.device_on_unknown)
                        } else {
                            ContextCompat.getString(this@FCMService, R.string.device_off_unknown)
                        }
                    )
                }
            }
        }
    }

    /**
     * Notification 띄워주는 함수
     */
    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent = PendingIntent.getActivity(
            this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = "wol_default_channel"
        val defaultSoundUrl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("전원 알림")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUrl)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    // Service 종료 시 코루틴 함께 종료
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}