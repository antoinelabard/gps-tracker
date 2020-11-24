package fr.labard.gpsgpx.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * LocationDao is used to give all the operations to access and modify the table responsible of the storage of the
 * locations.
 */
@Dao
interface LocationDao {

    @Query("SELECT * FROM location_table ORDER BY time ASC")
    fun getLocations(): LiveData<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(r: LocationEntity)
}