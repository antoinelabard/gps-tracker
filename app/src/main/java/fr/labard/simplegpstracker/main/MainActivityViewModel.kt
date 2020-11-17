package fr.labard.simplegpstracker.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.data.IRepository
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity

class MainActivityViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val allRecords = appRepository.getRecords()
    val allLocations = appRepository.getLocations()
    var records = listOf<RecordEntity>()
    var locations = listOf<LocationEntity>()

    fun insertRecord(recordEntity: RecordEntity) = appRepository.insertRecord(recordEntity)
    fun insertLocation(locationEntity: LocationEntity) = appRepository.insertLocation(locationEntity)
    fun deleteAll() = appRepository.deleteAll()
}

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        MainActivityViewModel(repository) as T
}