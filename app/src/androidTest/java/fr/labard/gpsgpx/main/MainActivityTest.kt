package fr.labard.gpsgpx.main

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fr.labard.gpsgpx.EspressoIdlingResource
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.ServiceLocator
import fr.labard.gpsgpx.data.IRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private lateinit var repository: IRepository

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        repository = ServiceLocator.provideAppRepository(getApplicationContext())
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
        onView(withId(R.id.activity_main_appbar)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_new_record_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_fragment_record_list)).check(matches(isDisplayed()))
    }
}