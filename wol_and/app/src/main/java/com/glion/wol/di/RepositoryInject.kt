package com.glion.wol.di

import com.glion.wol.data.repository.AuthRepositoryImpl
import com.glion.wol.data.repository.DeviceRepositoryImpl
import com.glion.wol.data.repository.InitializeRepositoryImpl
import com.glion.wol.data.repository.PushRepositoryImpl
import com.glion.wol.domain.repository.AuthRepository
import com.glion.wol.domain.repository.DeviceRepository
import com.glion.wol.domain.repository.InitializeRepository
import com.glion.wol.domain.repository.PushRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Project : WOL
 * File : RepositoryInject
 * Created by glion on 2025-09-04
 *
 * Description:
 * - Repository Interface Binding Module
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryInjectModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ) : AuthRepository

    @Binds
    @Singleton
    abstract fun bindInitializeRepository(
        initializeRepositoryImpl: InitializeRepositoryImpl
    ) : InitializeRepository

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(
        deviceRepositoryImpl: DeviceRepositoryImpl
    ) : DeviceRepository

    @Binds
    @Singleton
    abstract fun bindPushRepository(
        pushRepositoryImpl: PushRepositoryImpl
    ) : PushRepository
}