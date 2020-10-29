package fr.labard.simplegpstracker.model.data.local.db.location

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {

    @Query("SELECT * FROM location_table")
    fun getAll(): LiveData<List<LocationEntity>>

    @Query("SELECT * FROM location_table WHERE record_id = :recordId")
    fun getLocationsByRecordId(recordId: String): LiveData<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(r: LocationEntity)

    @Query("DELETE FROM location_table")
    fun deleteAll()
}