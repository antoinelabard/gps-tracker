package fr.labard.simplegpstracker.model.util

import android.location.Location

class LocationOps {
    fun getTotalDistance(locations: List<Location>): Float { // In m

        val l = locations.sortedBy { it.time }

        var d = 0f
        val s = locations.size
        for (i in 1 until s) {
            d += l[i].distanceTo(l[i - 1])
        }
        return d
    }

    fun getRecentSPeed(locations: List<Location>): Float { // In m/s

        if (locations.size < 2) return 0f

        val l = locations.sortedBy { it.time }

        val nLast = 3 // Number of locations to consider
        val d = listOf<Float>()

        for (i in 1 until l.size - 1) {
            if (i >= nLast - 1) break
            d.plus(l[i - 1].distanceTo(l[i]))
        }
        val t = (l.last().time - l.first().time) * 1000f
        return d.sum() / t
    }

    fun getTimeElapsed(locations: List<Location>): Long { // In ms
        if (locations.size < 2) return 0
        val l = locations.sortedBy { it.time }
        return l.last().time - l.first().time
    }
}