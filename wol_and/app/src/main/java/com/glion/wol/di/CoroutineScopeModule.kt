package com.glion.wol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Project : WOL
 * File : CoroutineScopeModule
 * Created by glion on 2025-09-29
 *
 * Description:
 * - Default Dispatchers 가진 CoroutineScope 주입
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @Provides
    @Singleton
    @ApplicationScopeDefault
    fun provideApplicationScopeDefault() : CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @Singleton
    @ApplicationScopeIO
    fun provideApplicationScopeIO() : CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}