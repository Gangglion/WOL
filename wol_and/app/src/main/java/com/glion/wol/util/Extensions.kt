package com.glion.wol.util

import java.util.Base64

/**
 * Project : WOL
 * File : Extensions
 * Created by glion on 2025-09-22
 *
 * Description:
 * - 확장함수 정의
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

fun ByteArray.b64Encode() = Base64.getEncoder().encodeToString(this)

fun String.b64DecodeByteArray() = Base64.getDecoder().decode(this)

fun String.b64DecodeStr() : String {
    val decode = Base64.getDecoder().decode(this)
    return String(decode, Charsets.UTF_8)
}

fun String.withColon(): String = this.chunked(2).joinToString(":")

fun String.deleteColon() : String = this.replace(":", "")