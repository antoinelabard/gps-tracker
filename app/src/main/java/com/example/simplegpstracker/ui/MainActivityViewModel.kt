package com.example.simplegpstracker.ui

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import com.example.simplegpstracker.model.db.AppRepository
import com.example.simplegpstracker.model.db.record.RecordEntity
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val mAppRepository = AppRepository(application)
    val allRecords = mAppRepository.allRecords
    var newId = 0

    fun insertNewRecord() {
        newId = 0
        val recordIds = getRecordsIds()
        while (true) {
            if (recordIds.find { it == newId } == null) break
            ++newId
        }
        val date = Date()
        insertRecord(RecordEntity(
            newId,
            "Record ${SimpleDateFormat("yyyy/mm/dd").format(date)}",
            date,
            date
        ))
    }

    fun insertRecord(recordEntity: RecordEntity?) = mAppRepository.insertRecord(recordEntity)

    fun getRecordsIds() = allRecords.value!!.map {it.id}
}