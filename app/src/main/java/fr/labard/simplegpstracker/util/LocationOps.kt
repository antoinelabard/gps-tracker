package fr.labard.simplegpstracker.util

import fr.labard.simplegpstracker.data.local.LocationEntity

class LocationOps {
    companion object {
        fun getTotalDistance(locations: List<LocationEntity>): Float { // In meters
            val l = locations.sortedBy { it.time }

            var d = 0f
            val s = locations.size
            for (i in 1 until s) {
                d += l[i].distanceTo(l[i - 1])
            }
            return d
        }

        fun getTotalTime(locations: List<LocationEntity>): Long { // in milliseconds
            if (locations.isEmpty()) return 0
            return locations.maxOf { it.time } - locations.minOf { it.time }
        }

        fun getRecentSPeed(locations: List<LocationEntity>) // In m/s
                = if (locations.isEmpty()) 0f else locations.last { l -> l.time == locations.maxOf { it.time } }.speed

        fun getAverageSpeed(locations: List<LocationEntity>) // In m/s
                = if (locations.isEmpty()) 0f else locations.map { it.speed }.average().toFloat()

        fun getMinSpeed(locations: List<LocationEntity>) // In m/s
                = if (locations.isEmpty()) 0f else locations.minOf { it.speed }

        fun getMaxSpeed(locations: List<LocationEntity>) // In m/s
                = if (locations.isEmpty()) 0f else locations.maxOf { it.speed }

        fun getNbLocations(locations: List<LocationEntity>)
                = locations.size
    }
}