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
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.util.Constants

class GpsService: Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var locationProvider: String? = null
    private var recordId = ""
    private var mode: String = Constants.Service.MODE_RECORD

    override fun onCreate() {
        super.onCreate()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationProvider = locationManager.getBestProvider(Criteria(), false)
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(
                Intent(Constants.Service.LOCATION_BROADCAST).apply {
                    putExtra(Constants.Intent.MODE, mode)
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
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        recordId = intent?.getStringExtra(Constants.Intent.RECORD_ID_EXTRA)!!
        mode = intent.getStringExtra(Constants.Intent.MODE)!!

        val playIntent = Intent(this, GpsService::class.java).apply {
            action = when (intent.action) {
                Constants.Intent.ACTION_PAUSE -> Constants.Intent.ACTION_PLAY
                else -> Constants.Intent.ACTION_PAUSE
            }
        }
        val playPendingIntent = PendingIntent.getForegroundService(
            this, 1, playIntent, 0)

        val stopIntent = Intent(this, GpsService::class.java).apply {
            action = Constants.Intent.ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getForegroundService(
            this, 1, stopIntent, 0)

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
                    applicationContext, 0, Intent(
                        applicationContext,
                        TrackerActivity::class.java
                    ).putExtra(Constants.Intent.RECORD_ID_EXTRA, recordId), 0
                )
            )
            setAutoCancel(true)
            addAction(R.drawable.ic_baseline_stop_24, getString(R.string.stop), stopPendingIntent)
        }

        when (intent.action) {
            Constants.Intent.ACTION_STOP -> stopSelf()
            Constants.Intent.ACTION_PAUSE -> {
                notification.addAction(R.drawable.ic_baseline_play_arrow_24, getString(R.string.play), playPendingIntent)
                disableLocationUpdates()
            }
            Constants.Intent.ACTION_PLAY -> {
                notification.addAction(R.drawable.ic_baseline_pause_24, getString(R.string.pause), playPendingIntent)
                enableLocationUpdates()
            }
        }

        startForeground(1, notification.build())
        return START_STICKY
    }

    private fun enableLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
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