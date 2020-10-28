package fr.labard.simplegpstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import java.util.*

class RecordListFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val recordLiveData = appRepository.getRecords()

    fun generateNewId(): Int {
        var newId = 0
        val recordIds = getRecordsIds()
        while (true) {
            if (recordIds.find { it == newId } == null) break
            ++newId
        }
        return newId
    }

    fun insertNewRecord(recordId: Int) {
        val date = Date()
        insertRecord(
            RecordEntity(
            recordId,
            "Record: $date",
            date,
            date
            )
        )
    }

    fun insertRecord(recordEntity: RecordEntity) = appRepository.insertRecord(recordEntity)

    private fun getRecordsIds() = recordLiveData.value!!.map {it.id}
}

@Suppress("UNCHECKED_CAST")
class RecordListFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (RecordListFragmentViewModel(repository) as T)
}