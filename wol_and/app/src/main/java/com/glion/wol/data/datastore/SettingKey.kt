package com.glion.wol.data.datastore

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * Project : WOL
 * File : SettingKey
 * Created by glion on 2025-09-03
 *
 * Description:
 * - DataStore 에 저장될 값의 키 관리
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

val CURRENT_URL = stringPreferencesKey("current_url")
val SELECTED_INDEX = longPreferencesKey("selected_index")
val HEADER_TOKEN = stringPreferencesKey("header_token")
val FCM_TOKEN = stringPreferencesKey("fcm_token")