package com.example.simplegpstracker.model.db.record

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(r: RecordEntity)

    @Query("SELECT * FROM record_table ORDER BY last_modification desc")
    fun getAll(): LiveData<List<RecordEntity>>

    @Query("DELETE FROM record_table WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM record_table")
    fun deleteAll()

    @Query("UPDATE record_table SET name = :name WHERE id = :id")
    fun updateName(id: Int, name: String)

    @Query("UPDATE record_table SET last_modification = :date WHERE id = :id")
    fun updateLastRecordModification(id: Int, date: Date)
}