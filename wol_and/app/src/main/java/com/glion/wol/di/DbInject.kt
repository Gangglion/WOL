package com.glion.wol.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.glion.wol.data.db.dao.DeviceDao
import com.glion.wol.data.db.datasource.DbDataSource
import com.glion.wol.data.db.datasource.DbDataSourceImpl
import com.glion.wol.data.db.entity.DeviceEntity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Project : WOL
 * File : DbInject
 * Created by glion on 2025-09-02
 *
 * Description:
 * - Room DB object, dataSource, repository 주입
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

@Database(entities = [DeviceEntity::class], version = 1)
abstract class WolDatabase: RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
}

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    @Singleton
    fun provideDbObject(
        @ApplicationContext applicationContext: Context
    ) : WolDatabase {
        return Room.databaseBuilder(
            applicationContext,
            WolDatabase::class.java, "wol-db"
        ).build()
    }

    @Provides
    fun provideDao(db: WolDatabase): DeviceDao = db.deviceDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DbDatasourceModule {
    @Binds
    @Singleton
    abstract fun bindDbDatasource(
        dbDataSourceImpl: DbDataSourceImpl
    ) : DbDataSource
}