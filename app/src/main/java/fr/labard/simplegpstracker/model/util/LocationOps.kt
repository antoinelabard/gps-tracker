package fr.labard.simplegpstracker.model.util

import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity

class LocationOps {
    fun getTotalDistance(locations: List<LocationEntity>): Float { // In meters
        val l = locations.sortedBy { it.time }

        var d = 0f
        val s = locations.size
        for (i in 1 until s) {
            d += l[i].distanceTo(l[i - 1])
        }
        return d
    }

    fun getTotalTime(locations: List<LocationEntity>) // in milliseconds
        = if (locations.isEmpty()) 0 else locations.maxOf { it.time } - locations.minOf { it.time }

    fun getRecentSPeed(locations: List<LocationEntity>) // In m/s
        = if (locations.isEmpty()) 0f else locations.first { l -> l.time == locations.maxOf { it.time } }.speed
    /*: Float {

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
    }*/

    fun getAverageSpeed(locations: List<LocationEntity>) // In m/s
            = if (locations.isEmpty()) 0f else getTotalDistance(locations) / locations.map { it.time }.sum()

    fun getMinSpeed(locations: List<LocationEntity>) // In m/s
            = locations.minOf { it.speed }

    fun getMaxSpeed(locations: List<LocationEntity>) // In m/s
            = locations.maxOf { it.speed }
}