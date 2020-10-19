package fr.labard.simplegpstracker.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.simplegpstracker.Data.Companion.PRECISION
import fr.labard.simplegpstracker.Data.Companion.dl12
import fr.labard.simplegpstracker.Data.Companion.dl123
import fr.labard.simplegpstracker.Data.Companion.dl1234
import fr.labard.simplegpstracker.Data.Companion.l1
import fr.labard.simplegpstracker.Data.Companion.l2
import fr.labard.simplegpstracker.Data.Companion.l3
import fr.labard.simplegpstracker.Data.Companion.tl12
import fr.labard.simplegpstracker.Data.Companion.tl13
import fr.labard.simplegpstracker.Data.Companion.tl14
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationOpsTest {

    private val locationOps = LocationOps()

    @Test
    fun getTotalDistance() {
        val expected = dl123
        val result = locationOps.getTotalDistance(listOf(l1, l2, l3))
        assertEquals(expected, result)
        assertTrue(result - PRECISION <= expected && expected <= result + PRECISION)
    }

    @Test
    fun getRecentSPeedNoLocation() {
        val expected = 0f
        val result = locationOps.getRecentSPeed(listOf())
        assertEquals(expected, result)
    }

    @Test
    fun getRecentSPeedTwoLocations() {
        val expected = dl12 / tl12
        val result = locationOps.getRecentSPeed(listOf(l1, l2))
        assertTrue(result - PRECISION <= expected && expected <= result + PRECISION)
    }

    @Test
    fun getRecentSPeedThreeLocations() {
        val expected = dl123 / tl13
        val result = locationOps.getRecentSPeed(listOf(l1, l2, l3))
        assertTrue(result - PRECISION <= expected && expected <= result + PRECISION)
    }

    @Test
    fun getRecentSPeedFourLocations() {
        val expected =  dl1234 / tl14
        val result = locationOps.getRecentSPeed(listOf(l1, l2, l3, l1))
        assertTrue(result - PRECISION <= expected && expected <= result + PRECISION)
    }


    @Test
    fun getTimeElapsedNoLocation() {
        val expected: Long = 0
        val result = locationOps.getTimeElapsed(listOf())
        assertEquals(expected, result)
    }

    @Test
    fun getTimeElapsedOneLocation() {
        val expected: Long = 0
        val result = locationOps.getTimeElapsed(listOf(l1))
        assertEquals(expected, result)
    }

    @Test
    fun getTimeElapsed() {
        val expected = l2.time - l1.time
        val result = locationOps.getTimeElapsed(listOf(l1, l2))
        assertEquals(expected, result)
    }
}