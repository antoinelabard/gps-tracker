package fr.labard.simplegpstracker.model.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.labard.simplegpstracker.GPSApplication
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import kotlinx.coroutines.launch
import java.util.*

class RecordListFragmentViewModel(
    application: Application,
    private val appRepository: IRepository
) : AndroidViewModel(application) {
    val allRecords = appRepository.getRecords()
    val allLocations = appRepository.getLocations()

    fun insertRecord() = viewModelScope.launch {
        val date = Date()
        appRepository.insertRecord(
            RecordEntity(
                getApplication<GPSApplication>().applicationContext.getString(R.string.new_record_name).format(date.toString()),
                date,
                date
            )
        )
    }
}

@Suppress("UNCHECKED_CAST")
class RecordListFragmentViewModelFactory (
    private val application: Application,
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (RecordListFragmentViewModel(application, repository) as T)
}