package com.example.simplegpstracker.model

class Constants {
    class Notification {
        companion object {
            const val CHANNEL_ID = "11"
            const val ACTION_PLAY_PAUSE = 12
            const val ACTION_STOP = 13
        }
    }
    class Permission {
        companion object {
            const val REQUEST_CODE = 21
        }
    }
    class LocationService {
        companion object {
            const val LOCATION_BROADCAST = "31"
            const val LOCATION_PROVIDER = ""
            const val MIN_TIME_REFRESH: Long = 400 // in milliseconds
            const val MIN_DISTANCE_REFRESH = 1.0f // in meters
        }
    }
    class Intent {
        companion object {
            const val LATITUDE_EXTRA = "latitude"
            const val LONGITUDE_EXTRA = "longitude"
            const val SPEED_EXTRA = "speed"
            const val RECORD_ID_EXTRA = "recordId"
            const val IS_RECORDING_EXTRA = "isRecording"
        }
    }
}