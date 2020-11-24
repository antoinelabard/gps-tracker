package fr.labard.gpsgpx.tracker

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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.util.Constants

/**
 * GpsService listens for location updates and sends them to the application.
 * The service is started when TrackerActivity is launched.
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
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
            setSmallIcon(R.mipmap.ic_launcher)
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

    override fun onLocationChanged(location: Location?) {
        location?.let {
            lastLocation.value = it
            // sends the location to the app as a broadcast
            LocalBroadcastManager.getInstance(this).sendBroadcast(
                Intent(Constants.Service.LOCATION_BROADCAST).apply {
                    putExtra(Constants.Service.GPS_MODE, gpsMode.value)
                    putExtra(Constants.Intent.RECORD_ID_EXTRA, activeRecordId.value)
                    putExtra(Constants.Intent.LATITUDE_EXTRA, location.latitude)
                    putExtra(Constants.Intent.LONGITUDE_EXTRA, location.longitude)
                    putExtra(Constants.Intent.SPEED_EXTRA, location.speed)
                    putExtra(Constants.Intent.TIME_EXTRA, location.time)
                }
            )
        }
    }

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