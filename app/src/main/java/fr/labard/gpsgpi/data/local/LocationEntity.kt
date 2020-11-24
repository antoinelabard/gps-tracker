package fr.labard.gpsgpi.data.local

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.labard.gpsgpi.util.Constants
import java.util.*

/**
 * LocationEntity provides all the columns for the table responsible of the storage of the locations.
 * A foreign key from RecordEntity is used to link the records in the database with its locations using the RecordEntity
 * id column.
 */
@Entity(
    tableName = Constants.Database.LOCATION_TABLE,
    foreignKeys = [ForeignKey(
        entity = RecordEntity::class,
        parentColumns = [Constants.Database.RECORD_ENTITY_ID],
        childColumns = [Constants.Database.LOCATION_ENTITY_RECORD_ID],
        onDelete = ForeignKey.CASCADE)])
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

    // id is not in the constructor because it is automatically generated
    @PrimaryKey
    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_ID)
    var id = UUID.randomUUID().toString()

    /**
     * Gives the distance between this object and another LocationEntity.
     * @param le the other location entity for which we want to know the distance to
     * @return the distance between this le and this LocationEntity
     */
    fun distanceTo(le: LocationEntity): Float {
        val l1 = Location("").apply {
            latitude = this@LocationEntity.latitude
            longitude = this@LocationEntity.longitude
        }
        val l2 = Location("").apply {
            latitude = le.latitude
            longitude = le.longitude
        }
        return l1.distanceTo(l2)
    }

    /**
     * Convert this LocationEntity into an object of the android Location class.
     * @return the location with all the data of this LocationEntity except its recordId
     */
    fun toLocation() = Location("").apply {
        latitude = this@LocationEntity.latitude
        longitude = this@LocationEntity.longitude
        speed = this@LocationEntity.speed
        time = this@LocationEntity.time
    }
}