package fr.labard.gpsgpx.tracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.gpsgpx.Data.Companion.le1
import fr.labard.gpsgpx.Data.Companion.le2
import fr.labard.gpsgpx.Data.Companion.le3
import fr.labard.gpsgpx.Data.Companion.r1
import fr.labard.gpsgpx.Data.Companion.r2
import fr.labard.gpsgpx.data.FakeTestRepository
import fr.labard.gpsgpx.data.local.Converters
import fr.labard.gpsgpx.data.local.FakeIDataSource
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackerActivityViewModelTest {

    private lateinit var fakeRepository: FakeTestRepository
    private lateinit var viewModel: TrackerActivityViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        fakeRepository = FakeTestRepository(
            FakeIDataSource(
                mutableListOf(r1, r2),
                mutableListOf(le1, le2)
            )
        )
        viewModel = TrackerActivityViewModel(fakeRepository)
        viewModel.activeRecordId = r1.id
    }

    @Test
    fun getRecordById() {
        val expected = r1
        val result = viewModel.getRecordById(r1.id)
        assertThat(result, `is`(expected))
    }

    @Test
    fun insertLocation() {
        val expected = mutableListOf(le1, le2, le3)
        viewModel.insertLocation(le3)
        val result = viewModel.allLocations.value.also { list ->
             list?.find { it.time == le3.time}?.id = le3.id
        }
        assertThat(result, `is`(expected))
    }

    @Test
    fun updateRecordName() {
        val expected = mutableListOf(r1.apply { name = "newName" }, r2)
        viewModel.updateRecordName("newName")
        val result = viewModel.allRecords.value as MutableList
        assertThat(result, `is`(expected))
    }

    @Test
    fun updateLastRecordModification() {
        val given = Converters().dateToTimestamp(r1.lastModification)
        Thread.sleep(5) // to make sure the dates are different
        viewModel.updateLastRecordModification()
        val result = Converters().dateToTimestamp(
            viewModel.allRecords.value?.find { it.id == r1.id }?.lastModification
        )
        assertTrue(result!! > given!!)
    }

    @Test
    fun deleteRecord() {
        val expected = mutableListOf(r1)
        viewModel.deleteRecord(r2.id)
        val result = viewModel.allRecords.value as MutableList
        assertThat(result, `is`(expected))
    }
}