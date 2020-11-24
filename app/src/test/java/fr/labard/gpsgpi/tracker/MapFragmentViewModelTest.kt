package fr.labard.gpsgpi.tracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.gpsgpi.Data.Companion.le1
import fr.labard.gpsgpi.Data.Companion.le3
import fr.labard.gpsgpi.Data.Companion.r1
import fr.labard.gpsgpi.data.FakeTestRepository
import fr.labard.gpsgpi.data.local.FakeIDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MapFragmentViewModelTest {

    private lateinit var fakeRepository: FakeTestRepository
    private lateinit var viewModel: MapFragmentViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        fakeRepository = FakeTestRepository(
            FakeIDataSource(
            mutableListOf(r1),
            mutableListOf(le1, le3)
        )
        )
        viewModel = MapFragmentViewModel(fakeRepository)
    }

    @Test
    fun setLocationsByActiveRecordId() {
        viewModel.activeRecordId = r1.id
        viewModel.setLocationsByActiveRecordId()
    }
}