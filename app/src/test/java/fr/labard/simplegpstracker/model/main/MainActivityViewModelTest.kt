package fr.labard.simplegpstracker.model.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.simplegpstracker.Data.Companion.le1
import fr.labard.simplegpstracker.Data.Companion.le2
import fr.labard.simplegpstracker.Data.Companion.r1
import fr.labard.simplegpstracker.Data.Companion.r2
import fr.labard.simplegpstracker.getOrAwaitValue
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

    private lateinit var mainActivityViewModel: MainActivityViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        mainActivityViewModel = MainActivityViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun insertNewRecord() {
        mainActivityViewModel.insertRecord(r1)

        assertThat(mainActivityViewModel.allRecords.getOrAwaitValue().first(), `is`(r1))
    }

    @Test
    fun insertNewLocation() {
        mainActivityViewModel.insertLocation(le1)

        assertThat(mainActivityViewModel.allLocations.getOrAwaitValue().first(), `is`(le1))
    }

    @Test
    fun deleteAll() {
        mainActivityViewModel.insertRecord(r1)
        mainActivityViewModel.insertRecord(r2)
        mainActivityViewModel.insertLocation(le1)
        mainActivityViewModel.insertLocation(le2)

        assertThat(mainActivityViewModel.allRecords.getOrAwaitValue(), `is`(listOf()))
        assertThat(mainActivityViewModel.allLocations.getOrAwaitValue(), `is`(listOf()))
    }
}