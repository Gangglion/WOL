package com.glion.wol

import android.app.Application
import com.glion.crypto_module.ExternalAESUtils
import dagger.hilt.android.HiltAndroidApp

/**
 * Project : WOL
 * File : WolApp
 * Created by glion on 2025-09-04
 *
 * Description:
 * - WOL 앱 진입점
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@HiltAndroidApp
class WolApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ExternalAESUtils.init(this)
    }
}