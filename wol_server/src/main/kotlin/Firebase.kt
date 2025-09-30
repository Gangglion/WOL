package com.glion

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import java.io.InputStream

fun Application.configureFirebase() {
    val serviceAccount: InputStream? = this::class.java.classLoader.getResourceAsStream("wol-fcm-service-account-key.json")

    val option = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    // Firebase App 초기화 여부 확인 후 중복 초기화 방지
    if(FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(option)
        println("Firebase has been initialized.")
    }
}