package com.example.simplegpstracker.model.db

import android.location.Location
import androidx.room.TypeConverter
import com.example.simplegpstracker.model.db.location.LocationEntity
import java.util.*

class Converters {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}