package fr.labard.gpsgpx.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import fr.labard.gpsgpx.AndroidData
import fr.labard.gpsgpx.AndroidData.Companion.r1
import fr.labard.gpsgpx.AndroidData.Companion.r2
import fr.labard.gpsgpx.androidGetOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RecordDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppRoomDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppRoomDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertRecordAndGet() = runBlockingTest {
        database.recordDao().insertRecord(r1)
        val result = database.recordDao().getRecords().androidGetOrAwaitValue()
        assertThat(result, `is`(listOf(r1)))

    }

    @Test
    fun updateRecordName() {
        database.recordDao().insertRecord(r1)
        database.recordDao().updateRecordName(r1.id, "newName")
        val result = database.recordDao().getRecords().androidGetOrAwaitValue()
        assertThat(result, `is`(listOf(r1.copy().apply { name = "newName" })))
    }

    @Test
    fun updateLastRecordModification() {
        val d = Date()
        database.recordDao().insertRecord(r1)
        database.recordDao().updateLastRecordModification(r1.id, d)
        val result = database.recordDao().getRecords().androidGetOrAwaitValue()
        assertThat(result, `is`(listOf(r1.apply { lastModification = d })))
    }

    @Test
    fun deleteRecord() {
        database.recordDao().insertRecord(r1)
        database.recordDao().deleteRecord(r1.id)
        val result = database.recordDao().getRecords().androidGetOrAwaitValue()
        assertThat(result, `is`(listOf()))
    }

    @Test
    fun deleteAll() {
        database.recordDao().insertRecord(r1)
        database.recordDao().insertRecord(r2)
        database.recordDao().deleteAll()
        val result = database.recordDao().getRecords().androidGetOrAwaitValue()
        assertThat(result, `is`(listOf()))
    }

    @Test
    fun deleteRecord_deleteLocationOnCascade() {
        database.recordDao().insertRecord(r1)
        database.locationDao().insertLocation(AndroidData.le1)
        database.locationDao().insertLocation(AndroidData.le2)
        database.recordDao().deleteRecord(r1.id)
        val resultRecords = database.recordDao().getRecords().androidGetOrAwaitValue()
        val resultLocations = database.locationDao().getLocations().androidGetOrAwaitValue()
        assertThat(resultRecords, `is`(listOf()))
        assertThat(resultLocations, `is`(listOf()))
    }
}
