package fr.labard.simplegpstracker.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.simplegpstracker.Data.Companion.r1
import fr.labard.simplegpstracker.Data.Companion.r2
import fr.labard.simplegpstracker.Data.Companion.r3
import fr.labard.simplegpstracker.FakeTestRepository
import fr.labard.simplegpstracker.MainCoroutineRule
import fr.labard.simplegpstracker.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
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

    private lateinit var fakeTestRepository: FakeTestRepository

    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setupViewModel() {
        fakeTestRepository = FakeTestRepository()
        fakeTestRepository.insertRecord(r1)
        fakeTestRepository.insertRecord(r2)

        viewModel = MainActivityViewModel(fakeTestRepository)
    }

    @Test
    fun insertRecord() {
        //When
        viewModel.insertRecord(r3)

        //Then
        val result = viewModel.allRecords.getOrAwaitValue()
        assertThat(result, `is`(listOf(r1, r2, r3)))

    }
}