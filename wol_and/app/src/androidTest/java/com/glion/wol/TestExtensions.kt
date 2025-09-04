package com.glion.wol

import com.glion.wol.util.Result
/**
 * Project : WOL
 * File : TestExtensions
 * Created by Gangglion on 2025-09-03
 *
 * Description:
 * - Success 를 제외한 나머지는 Exception Throw 하게 하여 테스트 시 결과를 바로 받아보기 위함
 *
 * Copyright @2025 Glion. All rights reserved
 */
fun <T> Result<T>.getOrThrow(): T = when(this) {
    is Result.Success -> data
    is Result.Error -> throw Exception("Result Error : ${this.errorMsg}")
    is Result.Loading -> throw Exception("Result is Loading")
}