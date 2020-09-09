package com.example.simplegpstracker.model.db.record

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(r: RecordEntity)

    @Query("SELECT * FROM record_table ORDER BY updated_date desc")
    fun getAll(): LiveData<List<RecordEntity>>

    @Query("DELETE FROM record_table WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM record_table")
    fun deleteAll()
}