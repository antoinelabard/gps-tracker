package fr.labard.simplegpstracker.model.data.local.db.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.labard.simplegpstracker.model.Constants
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity

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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_ID)
    var id: Int,

    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_RECORD_ID)
    var recordId: Int,

    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_TIME)
    var time: Long, // timestamp in milliseconds

    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_LATITUDE)
    var latitude: Double, // in degrees

    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_LONGITUDE)
    var longitude: Double, // in degrees

    @ColumnInfo(name = Constants.Database.LOCATION_ENTITY_SPEED)
    var speed: Float // in meters per seconds

    )