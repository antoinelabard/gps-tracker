package fr.labard.simplegpstracker.data.local

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.labard.simplegpstracker.util.Constants
import java.util.*

@Entity(
    tableName = Constants.Database.LOCATION_TABLE,
    foreignKeys = [ForeignKey(
        entity = RecordEntity::class,
        parentColumns = [Constants.Database.RECORD_ENTITY_ID],
        childColumns = [Constants.Database.LOCATION_ENTITY_RECORD_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocationEntity (
    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_RECORD_ID)
    var recordId: String,

    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_TIME)
    var time: Long, // timestamp in milliseconds

    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_LATITUDE)
    var latitude: Double, // in degrees

    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_LONGITUDE)
    var longitude: Double, // in degrees

    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_SPEED)
    var speed: Float // in meters per seconds

    ) {

    @PrimaryKey
    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_ID)
    var id = UUID.randomUUID().toString()

    fun distanceTo(l: LocationEntity): Float {
        val l1 = Location("").apply {
            latitude = this@LocationEntity.latitude
            longitude = this@LocationEntity.longitude
        }
        val l2 = Location("").apply {
            latitude = l.latitude
            longitude = l.longitude
        }
        return l1.distanceTo(l2)
    }

    fun distanceTo(l: Location): Float {
        val l1 = Location("").apply {
            latitude = latitude
            longitude = longitude
        }
        return l1.distanceTo(l)
    }

    fun toLocation() = Location("").apply {
        latitude = this@LocationEntity.latitude
        longitude = this@LocationEntity.longitude
        speed = this@LocationEntity.speed
        time = this@LocationEntity.time
    }
}