package fr.labard.simplegpstracker.model.db.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.RECORD_TABLE)
data class RecordEntity (
    @PrimaryKey
    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.RECORD_ENTITY_ID, index = true)
    var id: Int,

    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.RECORD_ENTITY_NAME)
    var name: String,

    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.RECORD_ENTITY_CREATION_DATE)
    var creationDate: Date,

    @ColumnInfo(name = _root_ide_package_.fr.labard.simplegpstracker.model.Constants.Database.RECORD_LAST_MODIFICATION)
    var lastModification: Date
) { fun clone(): RecordEntity = RecordEntity(id, name, creationDate, lastModification) }