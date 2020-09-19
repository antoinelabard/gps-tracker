package com.example.simplegpstracker.model

import android.app.Notification
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
import com.example.simplegpstracker.ui.TrackerActivity
import android.Manifest
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.simplegpstracker.R

class GpsService: Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var locationProvider: String? = null
    private lateinit var notification: Notification

    private var recordId: Int = 0

    override fun onCreate() {
        super.onCreate()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationProvider = locationManager.getBestProvider(Criteria(), false)

        notification = NotificationCompat.Builder(this, "1")
            .setContentTitle("Recording")
            .setContentText("Tap for more info")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(
                PendingIntent.getActivity(
                    applicationContext, 0, Intent(
                        applicationContext,
                        TrackerActivity::class.java
                    ).putExtra("recordId", recordId), 0
                )
            )
            .setAutoCancel(true)
            .build()
        Constants.Notification.CHANNEL_ID
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            val intent = Intent(Constants.LocationService.LOCATION_BROADCAST)
                .putExtra(Constants.Intent.LATITUDE_EXTRA, location.latitude)
                .putExtra(Constants.Intent.LONGITUDE_EXTRA, location.longitude)
                .putExtra(Constants.Intent.SPEED_EXTRA, location.speed)

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}
    override fun onProviderDisabled(provider: String?) {}

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        recordId = intent?.getIntExtra(Constants.Intent.RECORD_ID_EXTRA, 0)!!

        this.startForeground(1, notification)

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
        locationManager.requestLocationUpdates(locationProvider!!, Constants.LocationService.MIN_TIME_REFRESH, Constants.LocationService.MIN_DISTANCE_REFRESH, this)
        return START_STICKY
    }
}