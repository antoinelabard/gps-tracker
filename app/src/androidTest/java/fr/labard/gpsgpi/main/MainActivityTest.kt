package fr.labard.gpsgpi.main

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fr.labard.gpsgpi.EspressoIdlingResource
import fr.labard.gpsgpi.R
import fr.labard.gpsgpi.ServiceLocator
import fr.labard.gpsgpi.data.IRepository
import fr.labard.gpsgpi.util.DataBindingIdlingResource
import fr.labard.gpsgpi.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private lateinit var repository: IRepository
    private val dataBindingIdlingResource = DataBindingIdlingResource()

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
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }


    @Test
    fun elementsDisplayed() = runBlocking {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.activity_main_appbar)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_new_record_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_fragment_record_list)).check(matches(isDisplayed()))

        activityScenario.close()
    }
}