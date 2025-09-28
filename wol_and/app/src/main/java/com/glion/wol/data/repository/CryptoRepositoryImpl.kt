package com.glion.wol.data.repository

import com.glion.wol.data.crypto.dataSource.CryptoDataSource
import com.glion.wol.domain.repository.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : CryptoRepositoryImpl
 * Created by shhan on 2025-09-23
 *
 * Description:
 * - 추후 기입
 *
 * Copyright @2025 UBIPLUS. All rights reserved
 */
@Singleton
class CryptoRepositoryImpl @Inject constructor(
    private val dataSource: CryptoDataSource
) : CryptoRepository {
    override fun loadOrCreateRSAKey(): Flow<Unit> = flow {
        dataSource.loadOrCreateRSAKey()
        // 성공적으로 완료되었음을 알림
        emit(Unit)
    }.flowOn(Dispatchers.Default)

    override fun getRSAPublicKey(): Flow<ByteArray> = flow {
        emit(dataSource.getRsaPublicKey())
    }.flowOn(Dispatchers.Default)

    override suspend fun saveAESKey(encryptedAESKey: ByteArray): Flow<Unit> = flow {
        dataSource.saveAESKey(encryptedAESKey)
        // 성공적으로 완료되었음을 알림
        emit(Unit)
    }.flowOn(Dispatchers.Default)

    override fun loadAESKey(): Flow<Unit> = flow {
        dataSource.loadAESKey()
        emit(Unit)
    }.flowOn(Dispatchers.Default)

    override fun isExistAESKey(): Flow<Boolean> = flow {
        emit(dataSource.isExistAESKey())
    }.flowOn(Dispatchers.Default)
}