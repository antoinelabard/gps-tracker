package fr.labard.gpsgpx.util

import fr.labard.gpsgpx.data.local.LocationEntity

/**
 * Take the statistics data and return them with a better formatting for display.
 */
class StatisticsPresenter {
    companion object {
        fun getTotalDistanceFormatted(locations: List<LocationEntity>): String {
            val d = LocationOps.getTotalDistance(locations)
            return "${d}m"
        }

        fun getTotalTimeFormatted(locations: List<LocationEntity>): String { // in milliseconds
            val t = LocationOps.getTotalTime(locations) / 1000
            val s = t % 60
            val m = t / 60 % 60
            val h = t / 3600
            // the string looks like "00h00m00s"
            return (if (h != 0L) "${h}h" else "") + (if (m != 0L) "${m}m" else "") + "${s}s"
        }

        fun getRecentSPeedFormatted(locations: List<LocationEntity>): String {
            val s = LocationOps.getRecentSPeed(locations)
            return "${s * 3.6}km/h"
        }

       fun getAverageSpeedFormatted(locations: List<LocationEntity>): String {
           val s = LocationOps.getAverageSpeed(locations)
           return "${s * 3.6}km/h"
       }

       fun getMinSpeedFormatted(locations: List<LocationEntity>): String {
           val s = LocationOps.getMinSpeed(locations)
           return "${s * 3.6}km/h"
       }

        fun getMaxSpeedFormatted(locations: List<LocationEntity>): String {
            val s = LocationOps.getMaxSpeed(locations)
            return "${s * 3.6}km/h"
        }

        fun getNbLocationsPresented(locations: List<LocationEntity>): String
            = LocationOps.getNbLocations(locations).toString()
    }
}