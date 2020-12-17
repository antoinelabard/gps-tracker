package fr.labard.gpsgpx.tracker

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import fr.labard.gpsgpx.EspressoIdlingResource
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.ServiceLocator
import fr.labard.gpsgpx.data.IRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TrackerActivityTest {
    private lateinit var repository: IRepository

    @get:Rule
    val activityRule = ActivityScenarioRule(TrackerActivity::class.java)

    @Before
    fun init() {
        repository = ServiceLocator.provideAppRepository(ApplicationProvider.getApplicationContext())
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
        onView(withId(R.id.activity_tracker_appbar)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_tracker_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_tracker_fragment_container)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_map)).check(matches(isDisplayed()))
    }

    @Test
    fun fromMapFragment_showFollowFragment(): Unit = runBlocking {
        onView(withId(R.id.fragment_map)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_tracker_action_follow_mode)).perform(click())
        onView(withId(R.id.fragment_follow)).check(matches(isDisplayed()))
    }

    @Test
    fun fromFollowFragment_showMapFragment(): Unit = runBlocking {
        onView(withId(R.id.activity_tracker_action_follow_mode)).perform(click())
        onView(withId(R.id.fragment_follow)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_tracker_action_record_mode)).perform(click())
        onView(withId(R.id.fragment_map)).check(matches(isDisplayed()))
    }
}