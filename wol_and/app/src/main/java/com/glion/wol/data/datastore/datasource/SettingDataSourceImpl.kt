package com.glion.wol.data.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.glion.wol.data.datastore.FCM_TOKEN
import com.glion.wol.data.datastore.HEADER_TOKEN
import com.glion.wol.data.datastore.SELECTED_INDEX
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Project : WOL
 * File : DataStoreDataSourceImp
 * Created by glion on 2025-09-03
 *
 * Description:
 * - SettingDataSource Interface 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
class SettingDataSourceImpl @Inject constructor(
    private val ds: DataStore<Preferences>
) : SettingDataSource {
    override val selectedIndex: Flow<Long>
        get() = ds.data.map { pref ->
            pref[SELECTED_INDEX] ?: 0L
        }

    override suspend fun editSelectIndex(idx: Long) {
        ds.edit { pref ->
            pref[SELECTED_INDEX] = idx
        }
    }

    override val token: Flow<String?>
        get() = ds.data.map { pref ->
            pref[HEADER_TOKEN]
        }

    override suspend fun setToken(token: String) {
        ds.edit { pref ->
            pref[HEADER_TOKEN] = token
        }
    }

    override val fcmToken: Flow<String?>
        get() = ds.data.map { pref ->
            pref[FCM_TOKEN]
        }

    override suspend fun setFcmToken(fcmToken: String) {
        ds.edit { pref ->
            pref[FCM_TOKEN] = fcmToken
        }
    }
}