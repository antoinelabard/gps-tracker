package fr.labard.gpsgpx.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.gpsgpx.Data.Companion.le1
import fr.labard.gpsgpx.Data.Companion.le2
import fr.labard.gpsgpx.Data.Companion.le4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatisticsPresenterTest {

    @Test
    fun getTotalDistanceFormatted() {
        val expected = "${LocationOps.getTotalDistance(listOf(le1, le2))}m"
        val result = StatisticsPresenter.getTotalDistanceFormatted(listOf(le1, le2))
        assertEquals(expected, result)
    }

    @Test
    fun getTotalTimeFormatted() {
        val expected = "24h5m5s"
        val result = StatisticsPresenter.getTotalTimeFormatted(listOf(le1, le4))
        assertEquals(expected, result)
    }

    @Test
    fun getRecentSpeedFormatted() {
        val expected = "${LocationOps.getRecentSPeed(listOf(le1, le2)) * 3.6}km/h"
        val result = StatisticsPresenter.getRecentSPeedFormatted(listOf(le1, le2))
        assertEquals(expected, result)
    }

    @Test
    fun getAverageSpeedFormatted() {
        val expected = "${LocationOps.getAverageSpeed(listOf(le1, le2)) * 3.6}km/h"
        val result = StatisticsPresenter.getAverageSpeedFormatted(listOf(le1, le2))
        assertEquals(expected, result)
    }

    @Test
    fun getMinSpeedFormatted() {
        val expected = "${LocationOps.getMinSpeed(listOf(le1, le2)) * 3.6}km/h"
        val result = StatisticsPresenter.getMinSpeedFormatted(listOf(le1, le2))
        assertEquals(expected, result)
    }

    @Test
    fun getMaxSpeedFormatted() {
        val expected = "${LocationOps.getMaxSpeed(listOf(le1, le2)) * 3.6}km/h"
        val result = StatisticsPresenter.getMaxSpeedFormatted(listOf(le1, le2))
        assertEquals(expected, result)
    }

    @Test
    fun getNbLocationsFormatted() {
        val expected = "${LocationOps.getNbLocations(listOf(le1, le2))}"
        val result = StatisticsPresenter.getNbLocationsPresented(listOf(le1, le2))
        assertEquals(expected, result)
    }
}