package com.glion.wol.data.api

import com.glion.wol.BuildConfig
import com.glion.wol.data.datastore.datasource.SettingDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : UrlProvider
 * Created by glion on 2025-09-25
 *
 * Description:
 * - URL 상태 관리
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Singleton
class UrlProvider @Inject constructor(
    private val settingDataSource: SettingDataSource,
) {
    private val internalUrl = BuildConfig.DDNS_IN
    private val externalUrl = BuildConfig.DDNS_OUT

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val getUrlFlow: StateFlow<String> = settingDataSource.currentUrl
        .map { it ?: externalUrl }
        .stateIn(
            scope = coroutineScope, // 앱 전체 생명주기를 가진 코루틴 스코프
            started = SharingStarted.Eagerly, // 앱 시작 시 즉시 Flow 수집 시작
            initialValue = externalUrl // 초기 데이터 사용되기 전까지 사용할 기본값
        )

    fun getUrl() : String {
        return getUrlFlow.value
    }

    suspend fun setUrl(isInternalMode: Boolean) {
        val newUrl = if(isInternalMode) internalUrl else externalUrl
        settingDataSource.setUrl(newUrl)
    }
}