package fr.labard.simplegpstracker.model.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import kotlinx.coroutines.launch
import java.util.*

class RecordListFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val recordsLiveData = appRepository.getRecords()

    fun insertRecord() = viewModelScope.launch {
        val date = Date()
        appRepository.insertRecord(
            RecordEntity(
                "Record: $date",
                date,
                date
            )
        )
    }
}

@Suppress("UNCHECKED_CAST")
class RecordListFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (RecordListFragmentViewModel(repository) as T)
}