package com.example.simplegpstracker.model.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.simplegpstracker.model.db.location.LocationDao
import com.example.simplegpstracker.model.db.location.LocationEntity
import com.example.simplegpstracker.model.db.record.RecordDao
import com.example.simplegpstracker.model.db.record.RecordEntity
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
class AppRoomDatabaseTest : TestCase() {

    private lateinit var recordDao: RecordDao
    private lateinit var locationDao: LocationDao
    private lateinit var db: AppRoomDatabase

    val uId = 1000 // Default Id which belongs to no record nor location to test bad assignment behavior

    val r = RecordEntity(0, "r", Date(), Date()) // Default record
    val r1 = RecordEntity(1, "r1", Date(), Date()) // Second record
    val rConflict = RecordEntity(r.id, "rConflict", Date(), Date()) // Has the same id as r

    val l = LocationEntity(0, 0, 1.0, 0.0, 0.0f) // Default location
    val l1 = LocationEntity(0, 0, 2.0, 0.0, 0.0f) // Second location
    val lUnassigned = LocationEntity(2, uId, 4.0, 0.0, 0.0f) // Here the recordId belongs to no record intentionally

    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppRoomDatabase::class.java
        ).build()
        recordDao = db.recordDao()!!
        locationDao = db.locationDao()!!
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun canGetDb() = assertNotNull(db)

    @Test
    fun canGetRecordDao() = assertNotNull(recordDao)

    @Test
    fun canGetLocationDao() = assertNotNull(locationDao)


    @Test
    @Throws(Exception::class)
    fun insertAndGetAllRecords() {
        recordDao.insert(r)
        recordDao.insert(r1)
        val result = recordDao.getAll()
        result.observeForever{}
        assertThat(result.value?.toSet(), equalTo(setOf(r, r1)))
    }

    @Test
    @Throws(Exception::class)
    fun insertRecordReplaceOnIdConflict() {
        recordDao.insert(r)
        recordDao.insert(rConflict)
        val result = recordDao.getAll()
        result.observeForever{}
        assertThat(result.value?.get(0), equalTo(rConflict))
    }

    @Test
    @Throws(Exception::class)
    fun deleteRecordById() {
        recordDao.insert(r)
        recordDao.insert(r1)
        recordDao.deleteById(r1.id)
        val result = recordDao.getAll()
        result.observeForever{}
        assertThat(result.value, equalTo(listOf(r)))
    }

    @Test
    @Throws(Exception::class)
    fun deleteRecordByIdWithUnassignedIdDoNothing() {
        recordDao.insert(r)
        recordDao.insert(r1)
        recordDao.deleteById(uId)
        val result = recordDao.getAll()
        result.observeForever{}
        assertThat(result.value, equalTo(listOf(r, r1)))
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllRecords() {
        recordDao.insert(r)
        recordDao.insert(r1)
        recordDao.deleteAll()
        val result = recordDao.getAll()
        result.observeForever{}
        assertThat(result.value, equalTo(listOf()))
    }

    @Test
    @Throws(Exception::class)
    fun updateRecordName() {
        recordDao.insert(r)
        recordDao.updateName(r.id, "new")
        val result = recordDao.getAll()
        result.observeForever{}
        assertThat(result.value?.get(0)?.name, equalTo("new"))
    }

    @Test
    @Throws(Exception::class)
    fun updateRecordNameWithUnassignedIdDoNothing() {
        recordDao.insert(r)
        recordDao.updateName(uId, "new")
        val result = recordDao.getAll()
        result.observeForever{}
        assertThat(result.value?.get(0)?.name, equalTo(r.name))
    }

    @Test
    @Throws(Exception::class)
    fun updateLastRecordModification() {
        recordDao.insert(r)
        Thread.sleep(10)
        val d = Date()
        recordDao.updateLastRecordModification(r.id, d)
        val result = recordDao.getAll()
        result.observeForever{}
        assertThat(result.value?.get(0)?.lastModification, equalTo(d))
    }

    @Test
    @Throws(Exception::class)
    fun updateLastRecordModificationWithUnassignedIdDoNothing() {
        recordDao.insert(r)
        Thread.sleep(10)
        val d = Date()
        recordDao.updateLastRecordModification(uId, d)
        val result = recordDao.getAll()
        result.observeForever{}
        assertThat(result.value?.get(0)?.lastModification, equalTo(r.lastModification))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAllLocations() {
        val expect = setOf(
            l.copy().apply { id = 1 }, // the location id autoincrements and we must consider it
            l1.copy().apply { id = 2 }
        )
        recordDao.insert(r)
        locationDao.insert(l)
        locationDao.insert(l1)
        val result = locationDao.getAll()
        result.observeForever{}
        assertThat(result.value?.toSet(), equalTo(expect))
    }

    @Test(expected = android.database.sqlite.SQLiteConstraintException::class)
    fun insertLocationUnassignedRecordIdThrowsException() {
        locationDao.insert(lUnassigned)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllLocations() {
        recordDao.insert(r)
        locationDao.insert(l)
        locationDao.insert(l1)
        locationDao.deleteAll()
        val result = locationDao.getAll()
        result.observeForever{}
        assertThat(result.value, equalTo(listOf()))
    }


}