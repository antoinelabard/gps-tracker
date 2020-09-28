package com.example.simplegpstracker.model.db.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplegpstracker.model.Constants
import java.util.*

@Entity(tableName = Constants.Databalse.RECORD_TABLE)
data class RecordEntity (
    @PrimaryKey
    @ColumnInfo(name = Constants.Databalse.RECORD_ENTITY_ID, index = true)
    var id: Int,

    @ColumnInfo(name = Constants.Databalse.RECORD_ENTITY_NAME)
    var name: String,

    @ColumnInfo(name = Constants.Databalse.RECORD_ENTITY_CREATION_DATE)
    var creationDate: Date,

    @ColumnInfo(name = Constants.Databalse.RECORD_LAST_MODIFICATION)
    var lastModification: Date
) { fun clone(): RecordEntity = RecordEntity(id, name, creationDate, lastModification) }