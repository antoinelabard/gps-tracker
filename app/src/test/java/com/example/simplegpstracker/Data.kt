package com.example.simplegpstracker

import android.location.Location
import com.example.simplegpstracker.model.db.location.LocationEntity
import com.example.simplegpstracker.model.db.record.RecordEntity
import java.util.*
import kotlin.math.pow

class Data {
    companion object {
        val PRECISION = 10.0.pow(-2)

        const val unassignedId = 1000 // Default Id which belongs to no record nor location to test bad assignment behavior

        val r1 = RecordEntity(0, "r", Date(), Date()) // First record
        val r2 = RecordEntity(1, "r1", Date(), Date()) // Second record
        val rConflict = RecordEntity(r1.id, "rConflict", Date(), Date()) // Has the same id as r intentionally

        val lUnassigned = LocationEntity(0, unassignedId, 944006400000, 0.0, 0.0, 0.0f) // Here the recordId belongs to no record intentionally

        val l1 = Location("").apply { // First Location
            latitude = 0.0
            longitude = 0.0
            time = 944006400000 // 01/01/2000 00:00:00 in ms
        }
        val l2 = Location("").apply { // Second location
            latitude = 1.0
            longitude = 1.0
            time = 944006405000 // 01/01/2000 00:00:05 in ms
        }
        val l3 = Location("").apply { // ...
            latitude = -1.0
            longitude = 0.0
            time = 944006415000 // 01/01/2000 00:00:15 in ms
        }
        val l4 = Location("").apply {
            latitude = -2.0
            longitude = 0.0
            time = 944006430000 // 01/01/2000 00:00:30 in ms
        }

        val le1 = LocationEntity(
            0,
            0,
            l1.time,
            l1.latitude,
            l1.longitude,
            l1.speed
        )

        val le2 = LocationEntity(
            0,
            0,
            l2.time,
            l2.latitude,
            l2.longitude,
            l2.speed
        )

        val dl12 = l1.distanceTo(l2)
        val dl123 = l1.distanceTo(l2) + l2.distanceTo(l3)
        val dl1234 = l1.distanceTo(l2) + l2.distanceTo(l3) + l3.distanceTo(l4)
        val tl12: Float = l2.time - l1.time * 1000f
        val tl13: Float = l3.time - l1.time * 1000f
        val tl14: Float = l4.time - l1.time * 1000f
    }
}