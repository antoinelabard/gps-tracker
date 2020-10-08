package com.example.simplegpstracker.model

import android.location.Location
import junit.framework.TestCase
import org.junit.jupiter.api.Test

class LocationOpsTest : TestCase() {

    private val locationOps = LocationOps()

    private val l1 = Location("").apply {
        latitude = 0.0
        longitude = 0.0
        time = 944006400000 // 01/01/2000 00:00:00
    }
    private val l2 = Location("").apply {
        latitude = 1.0
        longitude = 1.0
        time = 944006405000 // 01/01/2000 00:00:05
    }
    private val l3 = Location("").apply {
        latitude = -1.0
        longitude = 0.0
        time = 944006415000 // 01/01/2000 00:00:15
    }


    @Test
    fun getTotalDistance(locations: List<Location>) {
        val expect = l1.distanceTo(l2) + l2.distanceTo(l3)
        val result = locationOps.getTotalDistance(listOf(l1, l2, l3))
        assertEquals(result, expect)
    }

    @Test
    fun getRecentSPeed(n: Int, locations: List<Location>) {
        TODO()
    }

    @Test
    fun getTimeElapsed() {
        val expected = l3.time - l1.time
        val result = locationOps.getTimeElapsed()
        assertEquals(expected, result)
    }
}