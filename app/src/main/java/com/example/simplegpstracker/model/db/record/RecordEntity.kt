package com.example.simplegpstracker.model.db.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "record_table")
data class RecordEntity (
    @PrimaryKey
    @ColumnInfo(name = "id", index = true)
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "creation_date")
    var creationDate: Date, // TODO: Replace the type of the variable by Date

    @ColumnInfo(name = "updated_date")
    var updatedDate: Date
) { fun clone(): RecordEntity = RecordEntity(id, name, creationDate, updatedDate) }