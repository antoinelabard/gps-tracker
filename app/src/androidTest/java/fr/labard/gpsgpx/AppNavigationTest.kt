package fr.labard.gpsgpx

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fr.labard.gpsgpx.data.IRepository
import fr.labard.gpsgpx.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest {

    private lateinit var tasksRepository: IRepository

    @Before
    fun init() {
        tasksRepository = ServiceLocator.provideAppRepository(getApplicationContext())
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun clickOnRecordItem_launchTrackerActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }
}