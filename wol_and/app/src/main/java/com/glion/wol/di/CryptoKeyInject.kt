package com.glion.wol.di

import com.glion.crypto_module.AESUtils
import com.glion.crypto_module.RSAUtils
import com.glion.wol.data.crypto.dataSource.CryptoKeyDataSource
import com.glion.wol.data.crypto.dataSource.CryptoKeyDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Project : WOL
 * File : CryptoInject
 * Created by glion on 2025-09-23
 *
 * Description:
 * - 암복호화 관련 의존성 주입
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CryptoKeyModule {
    @Binds
    @Singleton
    abstract fun bindsCryptoDataSource(
        cryptoDataSourceImpl: CryptoKeyDataSourceImpl
    ) : CryptoKeyDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object CryptoUtilModule {
    @Provides
    @Singleton
    fun provideRSAUtils() : RSAUtils{
        return RSAUtils()
    }

    @Provides
    @Singleton
    fun provideAESUtils() : AESUtils {
        return AESUtils()
    }
}