package fr.labard.simplegpstracker.data.local

import androidx.room.TypeConverter
import java.util.*

/**
 * Convert the java type Date to Long to make it compatible with the database.
 */
class Converters {

    @TypeConverter
    fun timestampToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time // in milliseconds
    }
}