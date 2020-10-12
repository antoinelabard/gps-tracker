package com.example.simplegpstracker.model

import android.location.Location
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.math.pow

@RunWith(JUnit4::class)
class LocationOpsTest : TestCase() {

    private val PRECISION = 10.0.pow(-2)

    private val locationOps = LocationOps()

    private val l1 = Location("").apply {
        latitude = 0.0
        longitude = 0.0
        time = 944006400000 // 01/01/2000 00:00:00 in ms
    }
    private val l2 = Location("").apply {
        latitude = 1.0
        longitude = 1.0
        time = 944006405000 // 01/01/2000 00:00:05 in ms
    }
    private val l3 = Location("").apply {
        latitude = -1.0
        longitude = 0.0
        time = 944006415000 // 01/01/2000 00:00:15 in ms
    }
    private val l4 = Location("").apply {
        latitude = -2.0
        longitude = 0.0
        time = 944006430000 // 01/01/2000 00:00:30 in ms
    }

    val dl12 = l1.distanceTo(l2)
    val dl123 = l1.distanceTo(l2) + l2.distanceTo(l3)
    val dl1234 = l1.distanceTo(l2) + l2.distanceTo(l3) + l3.distanceTo(l4)
    val tl12: Float = l2.time - l1.time * 1000f
    val tl13: Float = l3.time - l1.time * 1000f
    val tl14: Float = l4.time - l1.time * 1000f


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