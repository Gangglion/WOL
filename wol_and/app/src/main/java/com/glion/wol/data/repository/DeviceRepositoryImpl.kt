package com.glion.wol.data.repository

import com.glion.crypto_module.AESUtils
import com.glion.wol.data.api.data.RequestEncryptedCommon
import com.glion.wol.data.api.datasource.ApiDataSource
import com.glion.wol.data.crypto.dataSource.CryptoKeyDataSource
import com.glion.wol.data.datastore.datasource.SettingDataSource
import com.glion.wol.data.db.datasource.DbDataSource
import com.glion.wol.data.mapper.toEntity
import com.glion.wol.data.mapper.toModel
import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.model.remote.CommonResult
import com.glion.wol.domain.repository.DeviceRepository
import com.glion.wol.util.b64Encode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project : WOL
 * File : DeviceRepositoryImpl
 * Created by glion on 2025-10-13
 *
 * Description:
 * - DeviceRepository 구현체
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val roomDs: DbDataSource,
    private val apiDs: ApiDataSource,
    private val settingDs: SettingDataSource,
    private val cryptoKeyDs: CryptoKeyDataSource,
    private val aesUtils: AESUtils
) : DeviceRepository {
    override fun getAllDevice(): Flow<List<Device>> {
        return roomDs.getAllDevice().map { deviceList -> // Flow 가공
            deviceList.map { it.toModel() } // Flow 내의 데이터 Model로 변환
        }
    }

    override suspend fun getAlias(mac: String): String? = roomDs.getAlias(mac)

    override suspend fun changeMacAddr(id: Long, newMacAddr: String) {
        roomDs.changeMacAddr(id, newMacAddr)
    }

    override suspend fun changeAlias(id: Long, newAlias: String) {
        roomDs.changeAlias(id, newAlias)
    }

    override suspend fun changePowerStatus(macAddr: String, status: Boolean) = roomDs.changePowerStatus(macAddr, status)

    override suspend fun insertDevice(vararg device: Device) {
        roomDs.insertDevice(*device.map { it.toEntity() }.toTypedArray())
    }

    override suspend fun deleteDevice(device: Device): Boolean {
        val deleteCount = roomDs.deleteDevice(device.toEntity())
        if(deleteCount > 0) {
            return true
        } else {
            throw Exception("Nothing to Delete")
        }
    }

    // dataSource 에서 이미 Flow 를 넘겨주기 때문에, repository 에서 다시 flow 를 만들 필요는 없음
    override val selectedIndex: Flow<Long>
        get() = settingDs.selectedIndex

    override suspend fun editSelectedIndex(idx: Long) {
        settingDs.editSelectIndex(idx)
    }

    override suspend fun startDevice(mac: String): CommonResult {
        val aesKey = cryptoKeyDs.loadAESKey()
        val encryptedMac = aesUtils.encrypt(aesKey, mac)
        val body = RequestEncryptedCommon(
            encryptedDataBase64 = encryptedMac.first.b64Encode(),
            ivBase64 = encryptedMac.second.b64Encode()
        )
        return apiDs.startDevice(body).toModel()
    }
}