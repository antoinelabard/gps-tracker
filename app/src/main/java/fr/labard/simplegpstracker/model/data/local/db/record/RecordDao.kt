package fr.labard.simplegpstracker.model.data.local.db.record

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface RecordDao {

    @Query("SELECT * FROM record_table ORDER BY last_modification DESC")
    fun getAll(): LiveData<List<RecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(r: RecordEntity)

    @Query("UPDATE record_table SET name = :name WHERE id = :id")
    fun updateRecordName(id: String, name: String)

    @Query("UPDATE record_table SET last_modification = :date WHERE id = :id")
    fun updateLastRecordModification(id: String, date: Date)

    @Query("DELETE FROM record_table WHERE id = :id")
    fun deleteRecord(id: String)

    @Query("DELETE FROM record_table")
    fun deleteAll()
}