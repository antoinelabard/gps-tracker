package com.example.simplegpstracker.model.db.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.simplegpstracker.model.db.record.RecordEntity

@Entity(
    tableName = "location_table",
    foreignKeys = [ForeignKey(
        entity = RecordEntity::class,
        parentColumns = ["id"],
        childColumns = ["record_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocationEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "record_id")
    var recordId: Int,

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "longitude")
    var longitude: Double,

    @ColumnInfo(name = "speed")
    var speed: Float

    )