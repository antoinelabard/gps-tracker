package fr.labard.simplegpstracker.util

import fr.labard.simplegpstracker.data.local.LocationEntity

/**
 * This class is intended to provide some useful functions to manipulate Loaction data.
 */
class LocationOps {
    companion object {

        /**
         * Compute the total distance between the locations of the list provided, chronologically sorted.
         * @param locations the list of the locations
         * @return the total distance of the path drawn by the locations list
         */
        fun getTotalDistance(locations: List<LocationEntity>): Float { // In meters
            val l = locations.sortedBy { it.time }

            var d = 0f
            val s = locations.size
            for (i in 1 until s) {
                d += l[i].distanceTo(l[i - 1])
            }
            return d
        }

        /**
         * Compute the total time elapsed between the oldest and the most recent location.
         * @param locations the list of the locations
         * @return the time which separate the oldest and the most recent locations
         */
        fun getTotalTime(locations: List<LocationEntity>): Long { // in milliseconds
            if (locations.isEmpty()) return 0
            return locations.maxOf { it.time } - locations.minOf { it.time }
        }

        /**
         * give the speed of the most recent location in the list.
         * @param locations the list of locations
         * @return the speed of the most recent location
         */
        fun getRecentSPeed(locations: List<LocationEntity>) // In m/s
                = if (locations.isEmpty()) 0f else locations.last { l -> l.time == locations.maxOf { it.time } }.speed

        /**
         * give the average speed of the locations in the list.
         * @param locations the list of locations
         * @return the average speed
         */
        fun getAverageSpeed(locations: List<LocationEntity>) // In m/s
                = if (locations.isEmpty()) 0f else locations.map { it.speed }.average().toFloat()

        /**
         * give the minimum speed of the locations in the list.
         * @param locations the list of locations
         * @return the minimum speed
         */
        fun getMinSpeed(locations: List<LocationEntity>) // In m/s
                = if (locations.isEmpty()) 0f else locations.minOf { it.speed }

        /**
         * give the maximum speed of the locations in the list.
         * @param locations the list of locations
         * @return the maximum speed
         */
        fun getMaxSpeed(locations: List<LocationEntity>) // In m/s
                = if (locations.isEmpty()) 0f else locations.maxOf { it.speed }

        /**
         * give the number of locations in the list.
         * @param locations the list of locations
         * @return the size of the list
         */
        fun getNbLocations(locations: List<LocationEntity>)
                = locations.size
    }
}