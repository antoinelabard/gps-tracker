package fr.labard.simplegpstracker.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertRecord() {
        //Given
        val viewModel = MainActivityViewModel(ApplicationProvider.getApplicationContext())

        //When
        //operation

        //Then
        //assertion

    }
}