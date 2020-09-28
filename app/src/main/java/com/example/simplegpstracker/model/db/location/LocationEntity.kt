package com.example.simplegpstracker.model.db.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.simplegpstracker.model.Constants
import com.example.simplegpstracker.model.db.record.RecordEntity

@Entity(
    tableName = Constants.Databalse.LOCATION_TABLE,
    foreignKeys = [ForeignKey(
        entity = RecordEntity::class,
        parentColumns = [Constants.Databalse.RECORD_ENTITY_ID],
        childColumns = [Constants.Databalse.LOCATION_ENTITY_RECORD_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocationEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.Databalse.LOCATION_ENTITY_ID)
    var id: Int,

    @ColumnInfo(name = Constants.Databalse.LOCATION_ENTITY_RECORD_ID)
    var recordId: Int,

    @ColumnInfo(name = Constants.Databalse.LOCATION_ENTITY_LATITUDE)
    var latitude: Double,

    @ColumnInfo(name = Constants.Databalse.LOCATION_ENTITY_LONGITUDE)
    var longitude: Double,

    @ColumnInfo(name = Constants.Databalse.LOCATION_ENTITY_SPEED)
    var speed: Float

    )