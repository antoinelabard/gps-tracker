package fr.labard.gpsgpi.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.gpsgpi.data.IRepository
import fr.labard.gpsgpi.data.local.LocationEntity
import fr.labard.gpsgpi.data.local.RecordEntity

class MainActivityViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val allRecords = appRepository.getRecords()
    val allLocations = appRepository.getLocations()
    var records = listOf<RecordEntity>()
    var locations = listOf<LocationEntity>()

    fun insertRecord(recordEntity: RecordEntity) = appRepository.insertRecord(recordEntity)
    fun insertLocation(locationEntity: LocationEntity) = appRepository.insertLocation(locationEntity)
    fun deleteAll() = appRepository.clearAll()
}

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        MainActivityViewModel(repository) as T
}