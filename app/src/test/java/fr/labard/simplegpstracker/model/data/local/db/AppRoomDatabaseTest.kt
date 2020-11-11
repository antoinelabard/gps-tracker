package fr.labard.simplegpstracker.model.data.local.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.simplegpstracker.Data.Companion.lUnassigned
import fr.labard.simplegpstracker.Data.Companion.le1
import fr.labard.simplegpstracker.Data.Companion.le2
import fr.labard.simplegpstracker.Data.Companion.r1
import fr.labard.simplegpstracker.Data.Companion.r2
import fr.labard.simplegpstracker.Data.Companion.rConflict
import fr.labard.simplegpstracker.Data.Companion.unassignedId
import fr.labard.simplegpstracker.model.data.local.db.location.LocationDao
import fr.labard.simplegpstracker.model.data.local.db.record.RecordDao
import junit.framework.Assert.assertNotNull
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
class AppRoomDatabaseTest {

    private lateinit var recordDao: RecordDao
    private lateinit var locationDao: LocationDao
    private lateinit var db: AppRoomDatabase

    @JvmField
    @Rule
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
        recordDao.insertRecord(r1)
        recordDao.insertRecord(r2)
        val result = recordDao.getRecords()
        result.observeForever{}
        assertThat(result.value?.toSet(), equalTo(setOf(r1, r2)))
    }

    @Test
    @Throws(Exception::class)
    fun insertRecordReplaceOnIdConflict() {
        recordDao.insertRecord(r1)
        recordDao.insertRecord(rConflict)
        val result = recordDao.getRecords()
        result.observeForever{}
        assertThat(result.value?.get(0), equalTo(rConflict))
    }

    @Test
    @Throws(Exception::class)
    fun deleteRecordById() {
        recordDao.insertRecord(r1)
        recordDao.insertRecord(r2)
        recordDao.deleteRecord(r2.id)
        val result = recordDao.getRecords()
        result.observeForever{}
        assertThat(result.value, equalTo(listOf(r1)))
    }

    @Test
    @Throws(Exception::class)
    fun deleteRecordByIdWithUnassignedIdDoNothing() {
        recordDao.insertRecord(r1)
        recordDao.insertRecord(r2)
        recordDao.deleteRecord(unassignedId)
        val result = recordDao.getRecords()
        result.observeForever{}
        assertThat(result.value, equalTo(listOf(r1, r2)))
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllRecords() {
        recordDao.insertRecord(r1)
        recordDao.insertRecord(r2)
        recordDao.deleteAll()
        val result = recordDao.getRecords()
        result.observeForever{}
        assertThat(result.value, equalTo(listOf()))
    }

    @Test
    @Throws(Exception::class)
    fun updateRecordName() {
        recordDao.insertRecord(r1)
        recordDao.updateRecordName(r1.id, "new")
        val result = recordDao.getRecords()
        result.observeForever{}
        assertThat(result.value?.get(0)?.name, equalTo("new"))
    }

    @Test
    @Throws(Exception::class)
    fun updateRecordNameWithUnassignedIdDoNothing() {
        recordDao.insertRecord(r1)
        recordDao.updateRecordName(unassignedId, "new")
        val result = recordDao.getRecords()
        result.observeForever{}
        assertThat(result.value?.get(0)?.name, equalTo(r1.name))
    }

    @Test
    @Throws(Exception::class)
    fun updateLastRecordModification() {
        recordDao.insertRecord(r1)
        Thread.sleep(10)
        val d = Date()
        recordDao.updateLastRecordModification(r1.id, d)
        val result = recordDao.getRecords()
        result.observeForever{}
        assertThat(result.value?.get(0)?.lastModification, equalTo(d))
    }

    @Test
    @Throws(Exception::class)
    fun updateLastRecordModificationWithUnassignedIdDoNothing() {
        recordDao.insertRecord(r1)
        Thread.sleep(10)
        val d = Date()
        recordDao.updateLastRecordModification(unassignedId, d)
        val result = recordDao.getRecords()
        result.observeForever{}
        assertThat(result.value?.get(0)?.lastModification, equalTo(r1.lastModification))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAllLocations() {
        val expect = setOf(
            le1.copy().apply { id = "1" },
            le2.copy().apply { id = "2" }
        )
        recordDao.insertRecord(r1)
        locationDao.insert(le1)
        locationDao.insert(le2)
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
        recordDao.insertRecord(r1)
        locationDao.insert(le1)
        locationDao.insert(le1)
        locationDao.deleteAll()
        val result = locationDao.getAll()
        result.observeForever{}
        assertThat(result.value, equalTo(listOf()))
    }


}