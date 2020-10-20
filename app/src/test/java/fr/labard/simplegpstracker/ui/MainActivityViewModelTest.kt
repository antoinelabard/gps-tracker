package fr.labard.simplegpstracker.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.simplegpstracker.Data.Companion.r1
import fr.labard.simplegpstracker.Data.Companion.r2
import fr.labard.simplegpstracker.Data.Companion.r3
import fr.labard.simplegpstracker.FakeTestRepository
import fr.labard.simplegpstracker.MainCoroutineRule
import fr.labard.simplegpstracker.getOrAwaitValue
import fr.labard.simplegpstracker.model.db.record.RecordEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeTestRepository

    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setupViewModel() {
        // We initialise the tasks to 3, with one active and two completed
        repository = FakeTestRepository()
        repository.insertRecord(r1)
        repository.insertRecord(r2)
        repository.insertRecord(r3)

        viewModel = MainActivityViewModel(repository)
    }

    @Test
    fun insertRecord() {
        //When
        viewModel.insertRecord(r1)

        //Then
        val result = viewModel.allRecords.getOrAwaitValue()
        assertThat(result, listOf<RecordEntity>())

    }
}