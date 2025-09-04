package com.glion.wol

import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.glion.wol.data.datastore.datasource.SettingDataSourceImpl
import com.glion.wol.data.db.dao.DeviceDao
import com.glion.wol.data.db.datasource.DbDataSourceImpl
import com.glion.wol.data.repository.LocalRepositoryImpl
import com.glion.wol.di.WolDatabase
import com.glion.wol.domain.model.Device
import com.glion.wol.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Project : WOL
 * File : RoomTest
 * Created by Gangglion on 2025-09-03
 *
 * Description:
 * - Room Repository 테스트
 *
 * Copyright @2025 Glion. All rights reserved
 */
class LocalRepositoryTest {
    companion object {
        const val TEST_PREFS_FILE_NAME = "test_prefs.preferences_pb"
    }
    private lateinit var db: WolDatabase
    private lateinit var deviceDao: DeviceDao
    private lateinit var roomDataSource: DbDataSourceImpl
    private lateinit var settingDataSource: SettingDataSourceImpl
    private lateinit var repository: LocalRepositoryImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context,
            WolDatabase::class.java
        ).allowMainThreadQueries().build()
        val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = TestScope(UnconfinedTestDispatcher()),
            // TestScope 는 기본적으로 StandardTestDispatcher 사용됨(명시적으로 코루틴 진행을 시켜주어야 함 - 시간 조절이 필요한 테스트 코드에서 유용)
            // UnconfinedTestDispatcher 는 즉시 실행 가능한 Dispatcher 로서, 코루틴 시작시 즉시 현재 스레드에서 실행됨. DataStore 테스트 시 flow 를 collect 하거나 first 로 값을 가져올때 유용함.
            produceFile = { context.dataStoreFile(TEST_PREFS_FILE_NAME) }
        )
        deviceDao = db.deviceDao()
        roomDataSource = DbDataSourceImpl(deviceDao)
        settingDataSource = SettingDataSourceImpl(testDataStore)
        repository = LocalRepositoryImpl(roomDataSource, settingDataSource)
    }

    @After
    fun teardown() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = context.dataStoreFile(TEST_PREFS_FILE_NAME)
        file.delete()
        db.close()
    }


    /**
     * 기기 정보 저장 테스트
     */
    @Test
    fun insertDevice_success_returnTrue() = runTest {
        val device = Device(
            mac = "Test:Mac:Addr",
            alias = "Test",
            isPowerOn = false
        )
        val result = repository.insertDevice(device).first { it is Result.Success }.getOrThrow()
        assertTrue(result)
    }

    /**
     * 기기 정보 저장 후 모든 정보 조회하여 제대로 저장됬는지 테스트
     */
    @Test
    fun getDevice_returnsInsertDevice() = runTest {
        val device = Device(
            mac = "Test:Mac:Addr",
            alias = "Test",
            isPowerOn = false
        )
        val insert = repository.insertDevice(device).first { it is Result.Success }.getOrThrow()
        assertTrue(insert)
        val loaded = repository.getAllDevice().first { it is Result.Success }.getOrThrow()
        assertEquals(1, loaded.size)
        assertEquals("Test", loaded[0].alias)
        assertEquals("Test:Mac:Addr", loaded[0].mac)
        assertEquals(false, loaded[0].isPowerOn)
    }

    /**
     * 값 저장 후 isPowerOn, mac, alias 변경 테스트
     */
    @Test
    fun changeFunction_assertEqualsTest() = runTest {
        val device = Device(
            mac = "Test:Mac:Addr",
            alias = "Test",
            isPowerOn = false
        )
        val insert = repository.insertDevice(device).first { it is Result.Success }.getOrThrow()
        assertTrue(insert)
        val loaded = repository.getAllDevice().first { it is Result.Success }.getOrThrow()

        // isPowerOn 변경
        val changeIsPowerStatus = repository.changePowerStatus(loaded[0].mac, true)
            .first { it is Result.Success }.getOrThrow()
        assertTrue(changeIsPowerStatus)

        // mac 주소 변경
        val changeMac = repository.changeMacAddr(loaded[0].id, "Change:Mac:Addr")
            .first { it is Result.Success }.getOrThrow()
        assertTrue(changeMac)

        // alias 변경
        val changeAlias = repository.changeAlias(loaded[0].id, "Change")
            .first { it is Result.Success }.getOrThrow()
        assertTrue(changeAlias)

        // 변경된 상태 검증
        val loadedAfterChange = repository.getAllDevice().first { it is Result.Success }.getOrThrow()
        assertEquals(1, loadedAfterChange.size)
        assertEquals("Change", loadedAfterChange[0].alias)
        assertEquals("Change:Mac:Addr", loadedAfterChange[0].mac)
        assertEquals(true, loadedAfterChange[0].isPowerOn)
    }

    /**
     * 값 저장 후 특정 index 의 정보 찾기 테스트
     */
    @Test
    fun findTargetDevice_assertEqualsTest() = runTest {
        val device = Device(
            mac = "Test:Mac:Addr",
            alias = "Test",
            isPowerOn = false
        )
        val device2 = Device(
            mac = "Test:Mac:Addr2",
            alias = "Test2",
            isPowerOn = false
        )
        val device3 = Device(
            mac = "Test:Mac:Addr3",
            alias = "Test3",
            isPowerOn = false
        )
        val device4 = Device(
            mac = "Test:Mac:Addr4",
            alias = "Test4",
            isPowerOn = false
        )
        val insert = repository.insertDevice(device, device2, device3, device4).first { it is Result.Success }.getOrThrow()
        assertTrue(insert)
        val allData = repository.getAllDevice().first { it is Result.Success }.getOrThrow()
        assertEquals(4, allData.size)
        val find = repository.findTargetDevice(allData[1].id).first { it is Result.Success }.getOrThrow()
        assertEquals("Test2", find.alias)
        assertEquals("Test:Mac:Addr2", find.mac)
        assertEquals(false, find.isPowerOn)
    }

    /**
     * 값 저장 후 삭제 테스트
     */
    @Test
    fun deleteDevice_assertTrue() = runTest {
        val device = Device(
            mac = "Test:Mac:Addr",
            alias = "Test",
            isPowerOn = false
        )
        val insert = repository.insertDevice(device).first { it is Result.Success }.getOrThrow() // 삽입
        assertTrue(insert)
        val loadedBeforeDelete = repository.getAllDevice().first { it is Result.Success }.getOrThrow() // 삽입한 뒤 DB
        assertEquals(1, loadedBeforeDelete.size)
        val delete = repository.deleteDevice(loadedBeforeDelete[0]).first { it is Result.Success }.getOrThrow() // 삽입한 Device 객체 삭제
        assertTrue(delete)
        val loadAfterDelete = repository.getAllDevice().first { it is Result.Success }.getOrThrow() // 삭제 후 DB
        assertEquals(0, loadAfterDelete.size)
    }

    /**
     * DataStore 값 가져오기 테스트
     */
    @Test
    fun getSelectedIndex_assertEquals() = runTest {
        val selectedIndex = repository.selectedIndex.first { it is Result.Success }.getOrThrow()
        assertEquals(0L, selectedIndex)
    }

    /**
     * DataStore 에 값을 쓴 다음 가져와서 변경된 값 검증
     */
    @Test
    fun editSelectedIndex_assertEquals() = runTest {
        val edit = repository.editSelectedIndex(5).first { it is Result.Success }.getOrThrow()
        assertTrue(edit)
        val selectedIndex = repository.selectedIndex.first { it is Result.Success }.getOrThrow()
        assertEquals(5L, selectedIndex)
    }
}