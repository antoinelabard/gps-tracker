package fr.labard.simplegpstracker.data.local

import androidx.room.TypeConverter
import java.util.*

/**
 * Convert the java type Date to Long to make it compatible with the database.
 */
class Converters {

    /**
     * Return the date associated with the timestamp.
     *
     * @param timestamp the timestamp stored in the database
     * @return the date associated with this timestamp provided
     */
    @TypeConverter
    fun timestampToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    /**
     * Return the timestamp associated with the date.
     *
     * @param date the date
     * @return the to be stored in the database
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time // in milliseconds
    }
}