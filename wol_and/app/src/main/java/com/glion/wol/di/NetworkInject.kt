package com.glion.wol.di

import com.glion.wol.BuildConfig
import com.glion.wol.data.api.NeedHeaderWolService
import com.glion.wol.data.api.NoHeaderWolService
import com.glion.wol.data.api.RefreshTokenApi
import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.api.datasource.ApiDataSourceImpl
import com.glion.wol.data.api.interceptor.logInterceptor
import com.glion.wol.data.auth.AuthInterceptor
import com.glion.wol.data.auth.TokenAuthenticator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    private const val API_URL = BuildConfig.API_URL

    /**
     * 헤더가 필요한 네트워크 서비스 주입
     */
    @Singleton
    @Provides
    fun provideNeedHeaderNetworkService(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ) : NeedHeaderWolService {
        val okhttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(logInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator) // 401 요청 가로채서 토큰 갱신
            .build()

        return Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NeedHeaderWolService::class.java)
    }

    /**
     * 헤더가 필요없는 네트워크 서비스 주입
     */
    @Singleton
    @Provides
    fun provideNoHeaderNetworkService() : NoHeaderWolService {
        val okhttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(logInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoHeaderWolService::class.java)
    }

    /**
     * 토큰 갱신용 네트워크 서비스
     */
    @Singleton
    @Provides
    fun provideRefreshTokenService(
        authInterceptor: AuthInterceptor
    ) : RefreshTokenApi {
        val okhttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(logInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RefreshTokenApi::class.java)
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