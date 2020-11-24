package fr.labard.gpsgpx.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.gpsgpx.Data.Companion.PRECISION
import fr.labard.gpsgpx.Data.Companion.dl123
import fr.labard.gpsgpx.Data.Companion.le1
import fr.labard.gpsgpx.Data.Companion.le2
import fr.labard.gpsgpx.Data.Companion.le3
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationOpsTest {

    @Test
    fun getTotalDistance() {
        val expected = dl123
        val result = LocationOps.getTotalDistance(listOf(le1, le2, le3))
        assertEquals(expected, result)
        assertTrue(result - PRECISION <= expected && expected <= result + PRECISION)
    }
    @Test
    fun getTotalDistance_ZeroElement_returns0() {
        val expected = dl123
        val result = LocationOps.getTotalDistance(listOf(le1, le2, le3))
        assertEquals(expected, result)
        assertTrue(result - PRECISION <= expected && expected <= result + PRECISION)
    }
    @Test
    fun getTotalDistance_OneElement_returns0() {
        val expected = dl123
        val result = LocationOps.getTotalDistance(listOf(le1, le2, le3))
        assertEquals(expected, result)
        assertTrue(result - PRECISION <= expected && expected <= result + PRECISION)
    }


    @Test
    fun getTotalTime_ZeroElement_Returns0() {
        val expected: Long = 0
        val result = LocationOps.getTotalTime(listOf())
        assertEquals(expected, result)
    }

    @Test
    fun getTimeElapsed_OneElement_Returns0() {
        val expected: Long = 0
        val result = LocationOps.getTotalTime(listOf(le1))
        assertEquals(expected, result)
    }

    @Test
    fun getTotalTime() {
        val expected = le3.time - le1.time
        val result = LocationOps.getTotalTime(listOf(le1, le2, le3))
        assertEquals(expected, result)
    }

    @Test
    fun getRecentSPeed_ZeroElement_returns0() {
        val expected = 0f
        val result = LocationOps.getRecentSPeed(listOf())
        assertEquals(expected, result)
    }

    @Test
    fun getRecentSpeed() {
        val expected = le2.speed
        val result = LocationOps.getRecentSPeed(listOf(le1, le2))
        assertEquals(expected, result)
    }

    @Test
    fun getAverageSPeed_ZeroElement_returns0() {
        val expected = 0f
        val result = LocationOps.getRecentSPeed(listOf())
        assertEquals(expected, result)
    }

    @Test
    fun getAverageSPeed_OneElement_returns0() {
        val expected = le1.speed
        val result = LocationOps.getAverageSpeed(listOf(le1))
        assertEquals(expected, result)
    }

    @Test
    fun getAverageSPeed() {
        val l = listOf(le1, le2, le3)
        val expected = l.map { it.speed }.average().toFloat()
        val result = LocationOps.getAverageSpeed(l)
        assertEquals(expected, result)
    }

    @Test
    fun getMinSpeed_ZeroElement_Returns0() {
        val expected = 0f
        val result = LocationOps.getMinSpeed(listOf())
        assertEquals(expected, result)
    }

    @Test
    fun getMinSpeed() {
        val l = listOf(le1, le2, le3)
        val expected = l.minOf { it.speed }
        val result = LocationOps.getMinSpeed(l)
        assertEquals(expected, result)
    }

    @Test
    fun getMaxSpeed_ZeroElement_Returns0() {
        val expected = 0f
        val result = LocationOps.getMinSpeed(listOf())
        assertEquals(expected, result)
    }

    @Test
    fun getMaxSpeed() {
        val l = listOf(le1, le2, le3)
        val expected = l.maxOf { it.speed }
        val result = LocationOps.getMaxSpeed(l)
        assertEquals(expected, result)
    }

    @Test
    fun getNbLocations() {
        val l = listOf(le1, le2, le3)
        val expected = l.size
        val result = LocationOps.getNbLocations(l)
        assertEquals(expected, result)
    }
}