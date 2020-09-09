package com.example.simplegpstracker.ui

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.simplegpstracker.model.db.AppRepository
import com.example.simplegpstracker.model.db.location.LocationEntity
import com.example.simplegpstracker.model.db.record.RecordEntity

class TrackerActivityViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val mAppRepository = AppRepository(application)
    val allRecords = mAppRepository.allRecords
    val allLocations = mAppRepository.allLocations

    var recordId: Int = 0
    var isRecording = false

    fun getRecordById(id: Int): RecordEntity? = allRecords.value?.filter { it.id == id }?.first()

    fun renameRecord(id: Int, name: String) {
        val recordEntity = allRecords.value?.find { it.id == id }?.clone()
        recordEntity?.name = name
        mAppRepository.insertRecord(recordEntity)
    }

    fun deleteRecord(recordId: Int) = mAppRepository.deleteRecord(recordId)

    fun insertLocation(location: Location, rid: Int) {
        mAppRepository.insertLocation(
            LocationEntity(
                0,
                rid,
                location.latitude,
                location.longitude,
                location.speed
            )
        )
    }

    fun getLocations() = allLocations.value

    fun getLocationsByRecordId(recordId: Int) = allLocations.value?.filter { it.recordId == recordId }
}

