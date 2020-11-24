package fr.labard.gpsgpx.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.labard.gpsgpx.EspressoIdlingResource.wrapEspressoIdlingResource
import fr.labard.gpsgpx.data.local.LocationEntity
import fr.labard.gpsgpx.data.local.RecordEntity

/**
 * AppRepository provides all the operations to access, modify and delete the data of the application.
 */
class AppRepository internal constructor(
    private val localDataSource: IDataSource,
) : IRepository {

    override var activeRecordId = MutableLiveData("")

    override fun getRecords(): LiveData<List<RecordEntity>> {
        wrapEspressoIdlingResource {
            return localDataSource.getRecords()
        }
    }

    override fun insertRecord(recordEntity: RecordEntity) {
        wrapEspressoIdlingResource {
            localDataSource.insertRecord(recordEntity)
        }
    }

    override fun updateRecordName(id: String, name: String) {
        wrapEspressoIdlingResource {
            localDataSource.updateRecordName(id, name)
        }
    }

    override fun updateLastRecordModification(id: String) {
        wrapEspressoIdlingResource {
            localDataSource.updateLastRecordModification(id)
        }
    }

    override fun deleteRecord(id: String) {
        wrapEspressoIdlingResource {
            localDataSource.deleteRecord(id)
        }
    }

    override fun getLocations(): LiveData<List<LocationEntity>> {
        wrapEspressoIdlingResource {
            return localDataSource.getLocations()
        }
    }

    override fun insertLocation(locationEntity: LocationEntity) {
        wrapEspressoIdlingResource {
            localDataSource.insertLocation(locationEntity)
        }
    }

    override fun clearAll() {
        wrapEspressoIdlingResource {
            localDataSource.clearAll()
        }
    }
}