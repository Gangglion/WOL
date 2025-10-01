package com.glion.wol.domain.usecase.splash

import com.glion.wol.util.FlowResult
import com.glion.wol.util.LogUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Project : WOL
 * File : InitializeUseCase
 * Created by shhan on 2025-09-23
 *
 * Description:
 * - 앱을 시작하기 위한 모든 단계 실행
 * 1. RSA 키 생성 or 로드
 * 2. AES 키 존재여부 확인 후 없으면 키 교환 API 실행, AES 키 파일에 저장
 * 3. 모두 성공적으로 끝나면 토큰 관련 UseCase 실행
 * 5. 완료 / 오류 / 로딩 보내준다
 *
 * Copyright @2025 UBIPLUS. All rights reserved
 */
class InitializeUseCase @Inject constructor(
    private val initializeKeyUseCase: InitializeKeyUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val sendFcmTokenUseCase: SendFcmTokenUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke() : Flow<FlowResult<Boolean>> {
        // 1. 키 준비 UseCase 호출
        return initializeKeyUseCase()
            .flatMapConcat {
                // 2. AccessToken 받아오는 UseCase 호출
                getTokenUseCase()
            }
            .flatMapConcat {
                // 3. FcmToken 전송하는 UseCase 호출
                sendFcmTokenUseCase()
            }
            .map<Unit, FlowResult<Boolean>> {
                FlowResult.Success(true)
            }
            .catch { throwable ->
                throwable.printStackTrace()
                LogUtil.e("InitializeUseCase has Error", throwable)
                emit(FlowResult.Error("", throwable.message ?: "Known Error"))
            }
    }
}