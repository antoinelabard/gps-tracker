package fr.labard.simplegpstracker.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.simplegpstracker.Data.Companion.le1
import fr.labard.simplegpstracker.Data.Companion.le2
import fr.labard.simplegpstracker.Data.Companion.r1
import fr.labard.simplegpstracker.Data.Companion.r2
import fr.labard.simplegpstracker.data.local.FakeIDataSource
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AppRepositoryTest {

    private lateinit var appRepository: AppRepository
    private lateinit var fakeDataSource: FakeIDataSource

    @Before
    fun createRepository() {
        fakeDataSource = FakeIDataSource(mutableListOf(r1), mutableListOf(le1, le2))

        appRepository = AppRepository(fakeDataSource)
    }

    @Test
    fun getRecords() {
        val expected = listOf(r1)
        val result = appRepository.getRecords()
        assertThat(result.value, `is`(expected))
    }

    @Test
    fun insertRecord() {
        val expected = listOf(r1, r2)
        appRepository.insertRecord(r2)
        val result = appRepository.getRecords()
        assertThat(result.value, `is`(expected))
    }

    @Test
    fun updateRecordName() {
        val expected = listOf(r1.also { it.name = "newName" })
        appRepository.updateRecordName(r1.id, "newName")
        val result = appRepository.getRecords()
        assertThat(result.value, `is`(expected))
    }

    @Test
    fun updateLastRecordModification() {
        val d = Date(0)
        val expected = listOf(r1.also { it.lastModification = d })
        appRepository.updateLastRecordModification(r1.id)
        val result = appRepository.getRecords()
        assertThat(result.value, `is`(expected))
    }

    @Test
    fun deleteRecord() {
        val expected = listOf<RecordEntity>()
        appRepository.deleteRecord(r1.id)
        val result = appRepository.getRecords()
        assertThat(result.value, `is`(expected))
    }
    @Test
    fun getLocations() {
        val expected = listOf(le1, le2)
        val result = appRepository.getLocations()
        assertThat(result.value, `is`(expected))
    }

    @Test
    fun insertLocation() {
        val expected = listOf(r1, r2)
        appRepository.insertRecord(r2)
        val result = appRepository.getRecords()
        assertThat(result.value, `is`(expected))
    }

    @Test
    fun deleteAll() {
        val expectedRecords = listOf<RecordEntity>()
        val expectedLocations = listOf<LocationEntity>()
        appRepository.deleteAll()
        val resultRecords = appRepository.getRecords()
        val resultLocations = appRepository.getLocations()
        assertThat(resultRecords.value, `is`(expectedRecords))
        assertThat(resultLocations.value, `is`(expectedLocations))
    }
}