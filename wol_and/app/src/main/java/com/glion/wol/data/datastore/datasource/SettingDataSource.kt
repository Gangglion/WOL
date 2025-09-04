package com.glion.wol.data.datastore.datasource

import kotlinx.coroutines.flow.Flow

/**
 * Project : WOL
 * File : DataStoreDataSource
 * Created by glion on 2025-09-03
 *
 * Description:
 * - DataStore 을 통해 할 동작 정의
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface SettingDataSource {
    val selectedIndex: Flow<Long>
    suspend fun editSelectIndex(idx: Long)
}