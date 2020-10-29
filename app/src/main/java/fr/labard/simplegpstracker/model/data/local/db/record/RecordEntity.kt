package fr.labard.simplegpstracker.model.data.local.db.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.labard.simplegpstracker.model.util.Constants
import java.util.*

@Entity(tableName = Constants.Database.RECORD_TABLE)
data class RecordEntity (

    @ColumnInfo(name = Constants.Database.RECORD_ENTITY_NAME)
    var name: String,

    @ColumnInfo(name = Constants.Database.RECORD_ENTITY_CREATION_DATE)
    var creationDate: Date,

    @ColumnInfo(name = Constants.Database.RECORD_LAST_MODIFICATION)
    var lastModification: Date
) {
    @PrimaryKey
    @ColumnInfo(name = Constants.Database.RECORD_ENTITY_ID, index = true)
    var id: String = UUID.randomUUID().toString()
}