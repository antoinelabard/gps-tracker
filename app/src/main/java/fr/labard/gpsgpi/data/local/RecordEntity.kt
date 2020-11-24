package fr.labard.gpsgpi.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.labard.gpsgpi.util.Constants
import java.util.*

/**
 * RecordEntity provides all the columns for the table responsible of the storage of the records.
 */
@Entity(tableName = Constants.Database.RECORD_TABLE)
data class RecordEntity (

    @ColumnInfo(name = Constants.Database.RECORD_ENTITY_NAME)
    var name: String,

    @ColumnInfo(name = Constants.Database.RECORD_ENTITY_CREATION_DATE)
    var creationDate: Date,

    @ColumnInfo(name = Constants.Database.RECORD_LAST_MODIFICATION)
    var lastModification: Date
) {

    // id is not in the constructor because it is automatically generated
    @PrimaryKey
    @ColumnInfo(name = Constants.Database.RECORD_ENTITY_ID, index = true)
    var id: String = UUID.randomUUID().toString()
}