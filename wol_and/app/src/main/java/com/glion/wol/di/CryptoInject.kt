package com.glion.wol.di

import com.glion.wol.data.crypto.dataSource.CryptoDataSource
import com.glion.wol.data.crypto.dataSource.CryptoDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Project : WOL
 * File : CryptoInject
 * Created by glion on 2025-09-23
 *
 * Description:
 * - 암복호화 DataSource 의존성 주입
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CryptoModule {
    @Binds
    @Singleton
    abstract fun bindsCryptoDataSource(
        cryptoDataSourceImpl: CryptoDataSourceImpl
    ) : CryptoDataSource
}