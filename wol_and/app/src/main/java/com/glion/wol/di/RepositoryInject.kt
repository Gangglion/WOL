package com.glion.wol.di

import com.glion.wol.data.repository.CryptoRepositoryImpl
import com.glion.wol.data.repository.LocalRepositoryImpl
import com.glion.wol.data.repository.RemoteRepositoryImpl
import com.glion.wol.domain.repository.CryptoRepository
import com.glion.wol.domain.repository.LocalRepository
import com.glion.wol.domain.repository.RemoteRepository
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
    abstract fun bindsLocalRepository(
        localRepositoryImpl: LocalRepositoryImpl
    ) : LocalRepository

    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        remoteRepositoryImpl: RemoteRepositoryImpl
    ) : RemoteRepository

    @Binds
    @Singleton
    abstract fun bindCryptoRepository(
        cryptoRepositoryImpl: CryptoRepositoryImpl
    ) : CryptoRepository
}