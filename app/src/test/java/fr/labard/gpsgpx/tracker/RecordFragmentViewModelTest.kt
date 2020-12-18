package fr.labard.gpsgpx.tracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.gpsgpx.Data.Companion.le1
import fr.labard.gpsgpx.Data.Companion.le3
import fr.labard.gpsgpx.Data.Companion.r1
import fr.labard.gpsgpx.data.FakeTestRepository
import fr.labard.gpsgpx.data.local.FakeIDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RecordFragmentViewModelTest {

    private lateinit var fakeRepository: FakeTestRepository
    private lateinit var viewModel: RecordFragmentViewModel

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
        viewModel = RecordFragmentViewModel(fakeRepository)
    }

    @Test
    fun setLocationsByActiveRecordId() {
        viewModel.activeRecordId = r1.id
        viewModel.setLocationsByActiveRecordId()
    }
}