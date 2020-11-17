package fr.labard.simplegpstracker

import android.location.Location
import androidx.annotation.VisibleForTesting
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity
import java.util.*
import kotlin.math.pow

@VisibleForTesting
class Data {
    companion object {
        val PRECISION = 10.0.pow(-2)

        const val unassignedId = "unassignedId" // Default Id which belongs to no record nor location to test bad assignment behavior

        val r1 = RecordEntity("r1", Date(), Date())
        val r2 = RecordEntity("r2", Date(), Date())
        val r3 = RecordEntity("r3", Date(), Date())
        val rConflict = RecordEntity("rConflict", Date(), Date()).apply {id = r1.id} // Has the same id as r intentionally

        val lUnassigned = LocationEntity(unassignedId, 944006400000, 0.0, 0.0, 0.0f) // Here the recordId belongs to no record intentionally

        @VisibleForTesting
        val l1 = Location("").apply {
            latitude = 0.0
            longitude = 0.0
            time = 946681200000 // 01/01/2000 00:00:00 in ms
            speed = 0f
        }
        val l2 = Location("").apply {
            latitude = 1.0
            longitude = 1.0
            time = 946681205000 // 01/01/2000 00:00:05 in ms
            speed = 1f
        }
        val l3 = Location("").apply {
            latitude = -1.0
            longitude = 0.0
            time = 946681215000 // 01/01/2000 00:00:15 in ms
            speed = 2f
        }
        val l4 = Location("").apply {
            latitude = -2.0
            longitude = 0.0
            time = 946767905000 // 02/01/2000 00:05:05 in ms
            speed = 3f
        }

        val le1 = LocationEntity(
            r1.id,
            l1.time,
            l1.latitude,
            l1.longitude,
            l1.speed
        )

        val le2 = LocationEntity(
            r1.id,
            l2.time,
            l2.latitude,
            l2.longitude,
            l2.speed
        )

        val le3 = LocationEntity(
            r2.id,
            l3.time,
            l3.latitude,
            l3.longitude,
            l3.speed
        )

        val le4 = LocationEntity(
            r3.id,
            l4.time,
            l4.latitude,
            l4.longitude,
            l4.speed
        )

        val dl12 = l1.distanceTo(l2)
        val dl123 = l1.distanceTo(l2) + l2.distanceTo(l3)
        val dl1234 = l1.distanceTo(l2) + l2.distanceTo(l3) + l3.distanceTo(l4)
        val tl12: Float = l2.time - l1.time * 1000f
        val tl13: Float = l3.time - l1.time * 1000f
        val tl14: Float = l4.time - l1.time * 1000f
    }
}