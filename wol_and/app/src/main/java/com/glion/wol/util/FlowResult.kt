package com.glion.wol.util

/**
 * Project : WOL
 * File : Result
 * Created by glion on 2025-09-02
 *
 * Description:
 * - 비동기 처리 상태관리 wrapper
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
sealed interface FlowResult<out T> {
    data class Success<T>(val data: T) : FlowResult<T>
    data class Error(val errorCode: String, val errorMsg: String) : FlowResult<Nothing>
    data object Loading: FlowResult<Nothing>
}

// T 를 붙여 공변성 보장
// 제네릭 T 는 Result 내부에서 밖으로만 나갈 수 있다는 뜻(읽기 전용)
// 공변성 덕분에 Result<T> 를 상속받는 타입은 다른 T 로 승격이 가능하다
// T 에 Long 이 들어왔을떄, 성공 시 Result<Long>, 실패 시 Result<Nothing> 이지만, 공변성 덕분에 Result<Long> 으로 승격 가능, Loading 도 마찬가지.
// 공변성을 보장함으로서 Flow 에서 emit 할때 각 상황에 맞도록 값을 보낼 수 있음