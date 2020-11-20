package fr.labard.simplegpstracker.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import fr.labard.simplegpstracker.AndroidData.Companion.le1
import fr.labard.simplegpstracker.AndroidData.Companion.r1
import fr.labard.simplegpstracker.androidGetOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class LocationDaoTest {

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
    fun insertLocationAndGet() = runBlockingTest {
        database.recordDao().insertRecord(r1)
        database.locationDao().insertLocation(le1)
        val result = database.locationDao().getLocations().androidGetOrAwaitValue()
        assertThat(result, `is`(listOf(le1)))

    }
}
