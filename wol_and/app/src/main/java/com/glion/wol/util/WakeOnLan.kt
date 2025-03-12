package com.glion.wol.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * Project : WOL
 * File : WakOnLan
 * Created by Gangglion on 2025-03-12
 *
 * Description:
 * - 매직패킷 보내는 Object
 *
 * Copyright @2025 Gangglion. All rights reserved
 */
object WakeOnLan {
    suspend fun sendMagicPacket(macAddr: String, ddns: String) {
        withContext(Dispatchers.IO) {
            try {
                val macBytes = macAddr.split(":").map { it.toInt(16).toByte() }.toByteArray()
                val magicPacket = ByteArray(102) { 0xFF.toByte() }

                for(item in 6 until macBytes.size step macBytes.size) {
                    System.arraycopy(macBytes, 0, magicPacket, item, macBytes.size)
                }

                val splitDdns = ddns.split(":")
                val ip = splitDdns[0]
                val port = splitDdns[1].toInt()
                val address = InetAddress.getByName(ip)
                val packet = DatagramPacket(magicPacket, magicPacket.size, address, port)

                DatagramSocket().use { sock ->
                    sock.send(packet)
                }
            } catch(e: Exception) {
                LogUtil.e("sendMagicPacket has Error", e)
            }
        }
    }
}