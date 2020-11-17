package fr.labard.simplegpstracker.util

class Constants {
    class Notification {
        companion object {
            const val CHANNEL_ID = "11"
            const val ACTION_PLAY_PAUSE = 12
            const val ACTION_STOP = 13
        }
    }
    class Intent {
        companion object {
            const val LATITUDE_EXTRA = "latitude"
            const val LONGITUDE_EXTRA = "longitude"
            const val SPEED_EXTRA = "speed"
            const val TIME_EXTRA = "time"
            const val RECORD_ID_EXTRA = "recordId"
            const val IS_RECORDING_EXTRA = "isRecording"
            const val ACTION_PAUSE = "pause"
            const val ACTION_PLAY = "record"
            const val ACTION_STOP = "stop"
            const val REQUEST_RECORD_ID = "requestRecordId"
            const val MODE = "mode"
            const val REQUEST_CODE = 21
            const val CREATE_FILE_REQUEST_CODE = 22
            const val OPEN_FILE_REQUEST_CODE = 23
            const val SAVE_FILE_REQUEST_CODE = 24

        }
    }
    class Service {
        companion object {
            const val LOCATION_BROADCAST = "31"
            const val LOCATION_PROVIDER = ""
            const val MIN_TIME_REFRESH = 400L // in milliseconds
            const val MIN_DISTANCE_REFRESH = 1.0f // in meters
            const val MODE_RECORD = "modeRecord"
            const val MODE_FOLLOW = "modeFollow"
            const val MODE = "mode"
        }
    }
    class Database {
        companion object {
            const val DATABASE_NAME = "AppRoomDatabase.db"
            const val RECORD_TABLE = "record_table"
            const val LOCATION_TABLE = "location_table"

            const val RECORD_ENTITY_ID = "id"
            const val RECORD_ENTITY_NAME = "name"
            const val RECORD_ENTITY_CREATION_DATE = "creation_date"
            const val RECORD_LAST_MODIFICATION = "last_modification"

            const val LOCATION_ENTITY_ID = "id"
            const val LOCATION_ENTITY_RECORD_ID = "record_id"
            const val LOCATION_ENTITY_TIME = "time"
            const val LOCATION_ENTITY_LATITUDE = "latitude"
            const val LOCATION_ENTITY_LONGITUDE = "longitude"
            const val LOCATION_ENTITY_SPEED = "speed"
        }
    }
    class Location {
        companion object {
            const val MIN_RADIUS = 20.0 // in meters
        }
    }
    class Gpx {
        companion object {
            const val TRKSEG = "trkseg"
            const val GPX = "gpx" // root label of the document
            const val TRK = "trk" // equivalent to record
            const val TRKPT = "trkpt" // equivalent to location
            const val ID = "id"
            const val NAME = "name"
            const val CREATIONDATE = "creationdate"
            const val LASTMODIFICATION = "lastmodification"
            const val TIME = "time"
            const val LATITUDE = "lat"
            const val LONGITUDE = "lon"
            const val SPEED = "speed"
        }
    }
}