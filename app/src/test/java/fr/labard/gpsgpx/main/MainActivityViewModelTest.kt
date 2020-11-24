package fr.labard.gpsgpx.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.gpsgpx.Data.Companion.le1
import fr.labard.gpsgpx.Data.Companion.le2
import fr.labard.gpsgpx.Data.Companion.le3
import fr.labard.gpsgpx.Data.Companion.r1
import fr.labard.gpsgpx.Data.Companion.r2
import fr.labard.gpsgpx.data.FakeTestRepository
import fr.labard.gpsgpx.data.local.FakeIDataSource
import fr.labard.gpsgpx.data.local.LocationEntity
import fr.labard.gpsgpx.data.local.RecordEntity
import fr.labard.gpsgpx.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityViewModelTest {

    private lateinit var fakeRepository: FakeTestRepository
    private lateinit var viewModel: MainActivityViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        fakeRepository = FakeTestRepository(
            FakeIDataSource(
            mutableListOf(r1),
            mutableListOf(le1, le2)
        )
        )
        viewModel = MainActivityViewModel(fakeRepository)
    }

    @Test
    fun insertRecord() {
        val expected = listOf(r1, r2)
            viewModel.insertRecord(r2)
        val result = viewModel.allRecords.getOrAwaitValue()
        assertThat(result, `is`(expected))
    }

    @Test
    fun insertLocation() {
        val expected = listOf(le1, le2, le3)
        viewModel.insertLocation(le3)
        val result = viewModel.allLocations.getOrAwaitValue()
        assertThat(result, `is`(expected))
    }

    @Test
    fun deleteAll() {
        val expectedRecords = listOf<RecordEntity>()
        val expectedLocations = listOf<LocationEntity>()
        viewModel.deleteAll()
        val resultRecords = viewModel.allRecords.getOrAwaitValue()
        val resultLocations = viewModel.allLocations.getOrAwaitValue()
        assertThat(resultRecords, `is`(expectedRecords))
        assertThat(resultLocations, `is`(expectedLocations))
    }
}