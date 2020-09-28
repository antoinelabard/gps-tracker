package com.example.simplegpstracker.ui

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import com.example.simplegpstracker.model.db.AppRepository
import com.example.simplegpstracker.model.db.location.LocationEntity
import com.example.simplegpstracker.model.db.record.RecordEntity

class MainActivityViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val mAppRepository = AppRepository(application)
    val allRecords = mAppRepository.allRecords
    fun insertRecord(recordEntity: RecordEntity?) = mAppRepository.insertRecord(recordEntity)

    fun getRecordsIds() = allRecords.value!!.map {it.id}
}