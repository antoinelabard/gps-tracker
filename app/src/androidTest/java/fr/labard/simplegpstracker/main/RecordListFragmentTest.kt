package fr.labard.simplegpstracker.main

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import fr.labard.simplegpstracker.AndroidData.Companion.r1
import fr.labard.simplegpstracker.FakeAndroidTestRepository
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.ServiceLocator
import fr.labard.simplegpstracker.data.IRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RecordListFragmentTest {

    private lateinit var repository: IRepository
    private lateinit var context: Context


    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        context = InstrumentationRegistry.getInstrumentation().context
        ServiceLocator.appRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun records_displayedInUI() {
        launchFragmentInContainer<RecordListFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.recyclerview_item_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_item_name)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_item_name)).check(matches(withText(r1.name)))
        onView(withId(R.id.recyclerview_item_creationdate)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_item_creationdate)).check(matches(withText(
            "Created: %s".format(r1.creationDate.toString()))))
        onView(withId(R.id.recyclerview_item_nblocations)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_item_nblocations)).check(matches(withText(
            "%s locations".format(repository.getLocations().value
                ?.filter { it.recordId == r1.id }?.count().toString()))))
    }

    @Test
    fun newRecord_displayedInUI() {
        launchFragmentInContainer<RecordListFragment>(themeResId = R.style.AppTheme)
        repository.deleteAll()
        onView(withId(R.id.activity_main_new_record_fab)).perform(click())
        onView(withId(R.id.recyclerview_item_layout)).check(matches(isDisplayed()))
    }
}