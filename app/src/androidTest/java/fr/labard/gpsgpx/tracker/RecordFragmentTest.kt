package fr.labard.gpsgpx.tracker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import fr.labard.gpsgpx.EspressoIdlingResource
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.ServiceLocator
import fr.labard.gpsgpx.data.IRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RecordFragmentTest {

    private lateinit var repository: IRepository
    private lateinit var context: Context

    @get:Rule
    val activityRule = ActivityScenarioRule(TrackerActivity::class.java)

    @Before
    fun init() {
        repository = ServiceLocator.provideAppRepository(ApplicationProvider.getApplicationContext())
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun elementsDisplayed(): Unit = runBlocking {
        onView(withId(R.id.fragment_record_mapview)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_record_fab)).check(matches(isDisplayed()))
    }
}