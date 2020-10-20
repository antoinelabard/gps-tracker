package fr.labard.simplegpstracker.ui

import androidx.lifecycle.ViewModel
import fr.labard.simplegpstracker.model.db.IRepository
import fr.labard.simplegpstracker.model.db.record.RecordEntity
import java.util.*

class MainActivityViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val allRecords = appRepository.getRecords()

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
        insertRecord(RecordEntity(
            recordId,
            "Record: $date",
            date,
            date
        ))
    }

    fun insertRecord(recordEntity: RecordEntity?) = appRepository.insertRecord(recordEntity)

    fun getRecordsIds() = allRecords.value!!.map {it.id}
}