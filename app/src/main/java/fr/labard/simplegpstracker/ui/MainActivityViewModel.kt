package fr.labard.simplegpstracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fr.labard.simplegpstracker.model.db.AppRepository
import fr.labard.simplegpstracker.model.db.record.RecordEntity
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val mAppRepository = AppRepository(application)
    val allRecords = mAppRepository.allRecords

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
            "Record $date",
            date,
            date
        ))
    }

    fun insertRecord(recordEntity: RecordEntity?) = mAppRepository.insertRecord(recordEntity)

    fun getRecordsIds() = allRecords.value!!.map {it.id}
}