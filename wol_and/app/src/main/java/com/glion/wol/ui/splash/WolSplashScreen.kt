package com.glion.wol.ui.splash

import android.Manifest
import android.os.Build
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glion.wol.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

/**
 * Project : WOL
 * File : SplashScreen
 * Created by glion on 2025-09-22
 *
 * Description:
 * - Splash 화면 - 키 교환, 토큰 획득,
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WolSplashScreen(
    sbHost: SnackbarHostState,
    navigateToMain: () -> Unit,
    viewModel: WolSplashViewModel = hiltViewModel()
) {
    // stateFlow 는 collectAsState() 해주지 않으면 시작하지 않음
    val uiState by viewModel.uiState.collectAsState()

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        )

        // 권한 요청을 반복하지 않도록 방지하는 Flag
        val permissionRequested = rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(uiState.isInitialize, permissionState.status) {
            // 1. 초기화 작업이 끝났는지 확인
            if(uiState.isInitialize) {
                // 권한이 이미 허용된 상태
                if(permissionState.status.isGranted) {
                    navigateToMain()
                } else {
                    // 권한이 없는 상태
                    if(!permissionRequested.value) {
                        // 아직 권한 요청을 한 적이 없을때 -> 요청
                        permissionState.launchPermissionRequest()
                        // 권한 요청했음을 저장
                        permissionRequested.value = true
                    } else {
                        // 이미 요청했는데 거부한 상태
                        viewModel.onNotificationPermissionDenied()
                    }
                }
            }
        }
    } else { // Android 13 미만 기기 - 권한 요청 필요 없음
        LaunchedEffect(uiState.isInitialize) {
            navigateToMain()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { message ->
            sbHost.showSnackbar(message)
        }
    }

    WolSplashContent()
}

@Composable
fun WolSplashContent() {
    val bounceAnim = remember { Animatable(0f) }
    val rotateAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while(true) {
            // 1. 튕기기 (위아래 3번 = 1.5초)
            bounceAnim.animateTo(
                targetValue = 2f,
                animationSpec = repeatable(
                    iterations = 3,
                    animation = tween(durationMillis = 300, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            // 2. 회전 (3바퀴 = 1080도, 1초)
            rotateAnim.animateTo(
                targetValue = 360f,
                animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            )

            bounceAnim.animateTo(
                targetValue = 0f,
                animationSpec = repeatable(
                    iterations = 1,
                    animation = tween(durationMillis = 300, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            rotateAnim.snapTo(0f)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.mipmap.ic_launcher_foreground), // 원하는 로고 아이콘
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    translationY = -50 * bounceAnim.value   // 위아래 튕김
                    rotationZ = rotateAnim.value            // 회전
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWolSplashContent() {
    WolSplashContent()
}