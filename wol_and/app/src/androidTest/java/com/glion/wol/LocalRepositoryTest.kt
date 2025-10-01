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
import com.glion.wol.domain.model.local.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

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

    // TemporaryFolder Rule 추가: 테스트마다 고유한 임시 폴더를 생성
    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var db: WolDatabase
    private lateinit var deviceDao: DeviceDao
    private lateinit var roomDataSource: DbDataSourceImpl
    private lateinit var settingDataSource: SettingDataSourceImpl
    private lateinit var localRepository: LocalRepositoryImpl

    // 테스트용 Coroutine Scope 변수 선언
    private lateinit var testScope: CoroutineScope

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context,
            WolDatabase::class.java
        ).allowMainThreadQueries().build()
        testScope = CoroutineScope(UnconfinedTestDispatcher() + Job())
        val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = testScope,
            // TestScope 는 기본적으로 StandardTestDispatcher 사용됨(명시적으로 코루틴 진행을 시켜주어야 함 - 시간 조절이 필요한 테스트 코드에서 유용)
            // UnconfinedTestDispatcher 는 즉시 실행 가능한 Dispatcher 로서, 코루틴 시작시 즉시 현재 스레드에서 실행됨. DataStore 테스트 시 flow 를 collect 하거나 first 로 값을 가져올때 유용함.
            produceFile = { tempFolder.newFile(TEST_PREFS_FILE_NAME) }
        )
        deviceDao = db.deviceDao()
        roomDataSource = DbDataSourceImpl(deviceDao)
        settingDataSource = SettingDataSourceImpl(testDataStore)
        localRepository = LocalRepositoryImpl(roomDataSource, settingDataSource)
    }

    @After
    fun teardown() {
        db.close()
        testScope.cancel()
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
        localRepository.insertDevice(device)
        val loaded = localRepository.getAllDevice().first()
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
        localRepository.insertDevice(device)
        val loaded = localRepository.getAllDevice().first()

        // isPowerOn 변경
        localRepository.changePowerStatus(loaded[0].mac, true)

        // mac 주소 변경
        localRepository.changeMacAddr(loaded[0].id, "Change:Mac:Addr")

        // alias 변경
        localRepository.changeAlias(loaded[0].id, "Change")

        // 변경된 상태 검증
        val loadedAfterChange = localRepository.getAllDevice().first()
        assertEquals(1, loadedAfterChange.size)
        assertEquals("Change", loadedAfterChange[0].alias)
        assertEquals("Change:Mac:Addr", loadedAfterChange[0].mac)
        assertEquals(true, loadedAfterChange[0].isPowerOn)
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
        // 데이터 Insert
        localRepository.insertDevice(device)
        // Insert 후 데이터 조회
        val loadedBeforeDelete = localRepository.getAllDevice().first()
        assertEquals(1, loadedBeforeDelete.size)
        // Data 삭제
        val delete = localRepository.deleteDevice(loadedBeforeDelete[0])
        assertTrue(delete)
        // 삭제 후 DB 확인
        val loadAfterDelete = localRepository.getAllDevice().first() // 삭제 후 DB
        assertEquals(0, loadAfterDelete.size)
    }

    /**
     * DataStore 값 가져오기 테스트
     */
    @Test
    fun getSelectedIndex_assertEquals() = runTest {
        val selectedIndex = localRepository.selectedIndex.first()
        assertEquals(0L, selectedIndex)
    }

    /**
     * DataStore 에 값을 쓴 다음 가져와서 변경된 값 검증
     */
    @Test
    fun editSelectedIndex_assertEquals() = runTest {
        localRepository.editSelectedIndex(5)
        val selectedIndex = localRepository.selectedIndex.first()
        assertEquals(5L, selectedIndex)
    }
}