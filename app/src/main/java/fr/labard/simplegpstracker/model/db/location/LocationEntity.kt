package fr.labard.simplegpstracker.model.db.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.simplegpstracker.model.db.record.RecordEntity

@Entity(
    tableName = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.LOCATION_TABLE,
    foreignKeys = [ForeignKey(
        entity = RecordEntity::class,
        parentColumns = [_root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.RECORD_ENTITY_ID],
        childColumns = [_root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.LOCATION_ENTITY_RECORD_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocationEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.LOCATION_ENTITY_ID)
    var id: Int,

    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.LOCATION_ENTITY_RECORD_ID)
    var recordId: Int,

    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.LOCATION_ENTITY_TIME)
    var time: Long, // timestamp in milliseconds

    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.LOCATION_ENTITY_LATITUDE)
    var latitude: Double, // in degrees

    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.LOCATION_ENTITY_LONGITUDE)
    var longitude: Double, // in degrees

    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.LOCATION_ENTITY_SPEED)
    var speed: Float // in meters per seconds

    )