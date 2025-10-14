package com.glion.wol.domain.repository

import com.glion.wol.domain.model.local.Device
import com.glion.wol.domain.model.remote.CommonResult
import kotlinx.coroutines.flow.Flow

/**
 * Project : WOL
 * File : DeviceRepository
 * Created by glion on 2025-10-13
 *
 * Description:
 * - 기기 관리 Repository
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
interface DeviceRepository {
    /**
     * DB 에 저장된 모든 기기정보 가져오기
     * @return 기기 List
     */
    fun getAllDevice() : Flow<List<Device>>

    /**
     * 맥 주소에 따른 별칭 가져오기
     * @param mac 맥 주소
     * @return 맥 주소에 해당하는 별칭
     */
    suspend fun getAlias(mac: String) : String?

    /**
     * 맥주소 변경
     * @param id 기기 id
     * @param newMacAddr 변경할 맥 주소
     * @return Unit
     */
    suspend fun changeMacAddr(id: Long, newMacAddr: String)

    /**
     * 별칭 변경
     * @param id 기기 id
     * @param newAlias 변경할 별칭
     * @return Unit
     */
    suspend fun changeAlias(id: Long, newAlias: String)

    /**
     * 기기 전원상태 관리
     * @param macAddr 전원상태를 변경할 맥 주소
     * @param status 전원상태
     * @return Unit
     */
    suspend fun changePowerStatus(macAddr: String, status: Boolean)

    /**
     * 기기 추가
     * @param device 추가할 device
     * @return Unit
     */
    suspend fun insertDevice(vararg device: Device)

    /**
     * 기기 삭제
     * @param device 삭제할 Device 객체
     * @return 삭제 완료 여부
     */
    suspend fun deleteDevice(device: Device) : Boolean

    /**
     * 선택된 기기 Index
     */
    val selectedIndex: Flow<Long>

    /**
     * 선택한 index 수정 - 일회성 동작으로 반환값 없음
     * @param 새로 선택한 기기 Index
     */
    suspend fun editSelectedIndex(idx: Long)

    /**
     * 기기 전원 켜기
     * @param mac 전원을 켤 맥 주소
     * @return 결과 공통 객체
     */
    suspend fun startDevice(mac: String) : CommonResult
}