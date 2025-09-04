package com.glion.wol.data.mapper

import com.glion.wol.data.db.entity.DeviceEntity
import com.glion.wol.domain.model.Device

/**
 * Project : WOL
 * File : DbMapper
 * Created by glion on 2025-09-02
 *
 * Description:
 * - Entity <-> Model Mapper
 *
 * Copyright @2025 Gangglion. All rights reserved
 */

/**
 * Entity -> Model
 */
fun DeviceEntity.toModel(): Device =
    Device(
        id = this.id,
        mac = this.macAddr,
        alias = this.alias,
        isPowerOn = this.isPowerOn
    )

/**
 * Model -> Entity
 */
fun Device.toEntity(): DeviceEntity =
    DeviceEntity(
        id = this.id,
        macAddr = this.mac,
        alias = this.alias,
        isPowerOn = this.isPowerOn
    )