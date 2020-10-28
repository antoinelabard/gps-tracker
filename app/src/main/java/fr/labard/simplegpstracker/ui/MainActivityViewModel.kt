package fr.labard.simplegpstracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import java.util.*

class MainActivityViewModel(
    private val appRepository: IRepository
) : ViewModel() {
//    val allRecords = appRepository.getRecords()
//
//    fun generateNewId(): Int {
//        var newId = 0
//        val recordIds = getRecordsIds()
//        while (true) {
//            if (recordIds.find { it == newId } == null) break
//            ++newId
//        }
//        return newId
//    }
//
//    fun insertNewRecord(recordId: Int) {
//        val date = Date()
//        insertRecord(RecordEntity(
//            recordId,
//            "Record: $date",
//            date,
//            date
//        ))
//    }
//
//    fun insertRecord(recordEntity: RecordEntity) = appRepository.insertRecord(recordEntity)
//
//    private fun getRecordsIds() = allRecords.value!!.map {it.id}
}

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory (
    private val tasksRepository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MainActivityViewModel(tasksRepository) as T)
}