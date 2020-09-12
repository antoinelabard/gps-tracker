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
import com.example.simplegpstracker.R
import com.example.simplegpstracker.ui.TrackerActivity
import android.Manifest
import com.example.simplegpstracker.model.db.AppRepository
import com.example.simplegpstracker.model.db.location.LocationEntity

class GpsService: Service(), LocationListener {

    lateinit var locationManager: LocationManager
    var locationProvider: String? = null
    lateinit var notification: Notification
    val mAppRepository = AppRepository(application)

    var rid: Int = 0
    var location: Location? = null

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
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
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}
    override fun onProviderDisabled(provider: String?) {}

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

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
                    ), 0
                )
            )
            .setAutoCancel(true)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        rid = intent?.getIntExtra("recordId", 0)!!

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
        locationManager.requestLocationUpdates(locationProvider!!, 400, 1.0f, this)
        return START_STICKY
    }
}