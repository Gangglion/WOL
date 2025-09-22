package com.glion.wol.di

import com.glion.wol.BuildConfig
import com.glion.wol.data.api.WolService
import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.api.datasource.ApiDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Project : WOL
 * File : NetworkInject
 * Created by glion on 2025-09-22
 *
 * Description:
 * - 네트워크 관련 의존성 주입
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * 네트워크 클라이언트 주입
     */
    @Singleton
    @Provides
    fun provideNetworkInterface() : WolService {
        val baseUrl = BuildConfig.DDNS_OUT
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val okhttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(logInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WolService::class.java)
    }
}

/**
 * ApiDataSource 주입
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ApiDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindsApiDataSource(
        apiDataSourceImpl: ApiDataSourceImpl
    ) : ApiDataSource
}