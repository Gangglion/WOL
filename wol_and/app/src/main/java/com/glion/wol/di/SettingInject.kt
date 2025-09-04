package com.glion.wol.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.glion.wol.data.datastore.datasource.SettingDataSource
import com.glion.wol.data.datastore.datasource.SettingDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Project : WOL
 * File : PrefInject
 * Created by glion on 2025-09-03
 *
 * Description:
 * - DataStore 객체, dataSource, repository 주입
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@Module
@InstallIn(SingletonComponent::class)
object SettingModule {
    @Provides
    @Singleton
    fun provideDataStoreObject(
        @ApplicationContext context: Context
    ) : DataStore<Preferences> {
        return context.dataStore
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingDataSourceModule() {
    @Binds
    @Singleton
    abstract fun bindsSettingDataSource(
        settingDataSourceImpl: SettingDataSourceImpl
    ) : SettingDataSource
}