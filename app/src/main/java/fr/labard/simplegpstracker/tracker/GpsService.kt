package fr.labard.simplegpstracker.tracker

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.util.Constants

/**
 * GpsService listens for location updates and sends them to the application.
 * The service is started when the app is recording in whether record or follow mode.
 */
class GpsService: Service(), LocationListener {

    private val binder = LocalBinder()
    private lateinit var locationManager: LocationManager
    private var locationProvider: String? = null

    var gpsMode = MutableLiveData(Constants.Service.MODE_STANDBY)
    var activeRecordId = MutableLiveData<String>()
    val lastLocation = MutableLiveData<Location>()


    inner class LocalBinder : Binder() {
        fun getService(): GpsService = this@GpsService
    }

    override fun onBind(intent: Intent): IBinder {

        enableLocationUpdates()

        buildNotification()

        return binder
    }

    private fun buildNotification() {
        val notification = NotificationCompat.Builder(
            this, Constants.Notification.CHANNEL_ID).apply {
            setContentTitle(getString(R.string.gpstracker_notification_title))
            setContentText(getString(R.string.gpstracker_notification_content))
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setOngoing(true)
            setNotificationSilent()
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(
                PendingIntent.getActivity(
                    applicationContext,
                    Constants.Intent.REQUEST_CODE,
                    Intent(
                        applicationContext,
                        TrackerActivity::class.java
                    ),
                    0
                )
            )
            setAutoCancel(true)
        }

        startForeground(1, notification.build())
    }

    override fun onCreate() {
        super.onCreate()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationProvider = locationManager.getBestProvider(Criteria(), false)
    }

    override fun onLocationChanged(location: Location?) { location?.let {lastLocation.value = it} }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}

    private fun enableLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            stopSelf()
        }

        locationManager.requestLocationUpdates(
            locationProvider!!,
            Constants.Service.MIN_TIME_REFRESH,
            Constants.Service.MIN_DISTANCE_REFRESH,
            this
        )
    }

    private fun disableLocationUpdates() = locationManager.removeUpdates(this)

    override fun onDestroy() {
        super.onDestroy()
        disableLocationUpdates()
    }
}