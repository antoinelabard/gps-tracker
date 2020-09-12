package com.example.simplegpstracker.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.simplegpstracker.R
import com.example.simplegpstracker.model.GpsService
import com.example.simplegpstracker.model.db.Constants
import com.example.simplegpstracker.model.db.location.LocationEntity
import com.example.simplegpstracker.model.db.record.RecordEntity
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_tracker.*
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.concurrent.TimeUnit

class TrackerActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var mTrackerActivityViewModel: TrackerActivityViewModel
    private lateinit var mapController: IMapController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)
        setSupportActionBar(findViewById(R.id.activity_tracker_toolbar))
        mTrackerActivityViewModel = ViewModelProvider(this).get(TrackerActivityViewModel::class.java)
        mTrackerActivityViewModel.recordId = intent.getIntExtra("record_id", 0)

        Configuration.getInstance().load(applicationContext, getPreferences(Context.MODE_PRIVATE))
        buildMapView()

        mTrackerActivityViewModel.allRecords
            .observe(this, Observer<List<RecordEntity?>?> {
                activity_tracker_toolbar.title = mTrackerActivityViewModel.getRecordById(mTrackerActivityViewModel.recordId)?.name
                activity_tracker_toolbar.subtitle = mTrackerActivityViewModel.getLocationsByRecordId(mTrackerActivityViewModel.recordId)?.count().toString()
            })
        mTrackerActivityViewModel.allLocations
            .observe(this,
                Observer<List<LocationEntity?>?> {
                    val line = Polyline()
                    var parcours = mTrackerActivityViewModel.getLocationsByRecordId(mTrackerActivityViewModel.recordId)?.map {
                        GeoPoint(it.latitude, it.longitude)
                    }
                    if (parcours?.size == 0) parcours = mutableListOf(GeoPoint(0.0, 0.0))
                    line.setPoints(parcours)
                    activity_tracker_mapview.overlayManager.add(line)
                    mapController.setCenter(parcours?.last())
                }
            )

        when {
            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), Constants.Permission().REQUEST_CODE)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.Permission().REQUEST_CODE)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.INTERNET) -> {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET), Constants.Permission().REQUEST_CODE)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_NETWORK_STATE) -> {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_NETWORK_STATE), Constants.Permission().REQUEST_CODE)
            }
            else -> {
                buildLocationRequest()
                buildLocationCallBack()
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            }
        }
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    Constants.Permission().REQUEST_CODE
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, GpsService::class.java)
            .putExtra("recordId", mTrackerActivityViewModel.recordId)

        activity_tracker_pause_resume_button.setOnClickListener {
            if (mTrackerActivityViewModel.isRecording) {
                stopService(intent)
                activity_tracker_pause_resume_button.setImageResource(R.drawable.ic_action_gps_active)
            } else {
                Intent(this, TrackerService::class.java)
                    .putExtra("isRecording", true)
                    .also { intent -> startService(intent) }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                activity_tracker_pause_resume_button.setImageResource(R.drawable.ic_action_gps_inactive)
            }
            mTrackerActivityViewModel.isRecording = !mTrackerActivityViewModel.isRecording
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_tracker_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.activity_tracker_rename -> {
                val dialog_rename = layoutInflater.inflate(R.layout.dialog_rename, null)
                AlertDialog.Builder(this)
                    .setView(dialog_rename)
                    .setTitle(R.string.rename)
                    .setMessage(R.string.rename_message)
                    .setIcon(R.drawable.ic_action_rename)
                    .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
                        val rename_edittext = dialog_rename.findViewById<EditText>(R.id.rename_edittext)
                        val name = rename_edittext.text.toString()
                        if (name.isEmpty()) {
                            Toast.makeText(this, R.string.empty_name_error, Toast.LENGTH_LONG).show()
                        } else {
                            mTrackerActivityViewModel.renameRecord(mTrackerActivityViewModel.recordId, name)
//                            textview.text = mTrackerActivityViewModel.getRecords()?.filter { it.id == mTrackerActivityViewModel.recordId }.toString()
                        }
                    }
                    .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                        Toast.makeText(this, "canceled", Toast.LENGTH_LONG).show()
                    }
                    .show()
                true
            }
            R.id.activity_tracker_delete -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.delete)
                    .setMessage(R.string.delete_message)
                    .setIcon(R.drawable.ic_action_delete)
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        mTrackerActivityViewModel.deleteRecord(mTrackerActivityViewModel.recordId)
                        Snackbar.make(activity_tracker_layout, R.string.deletion_complete,Snackbar.LENGTH_LONG).show()
                        finish()
                    }
                    .setNegativeButton(R.string.no) { _: DialogInterface, _: Int ->
                        Toast.makeText(this, R.string.deletion_canceled, Toast.LENGTH_LONG).show()
                    }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest: ArrayList<String> = arrayListOf()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(arrayOf<String>()),
                Constants.Permission().REQUEST_CODE
            )
        }
    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                mTrackerActivityViewModel.insertLocation(locationResult!!.lastLocation, mTrackerActivityViewModel.recordId)
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = TimeUnit.SECONDS.toMillis(5)
            fastestInterval = TimeUnit.SECONDS.toMillis(3)
            smallestDisplacement = 10.0F
        }
    }

    private fun checkPermissionsGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), Constants.Permission().REQUEST_CODE)
            return false
        }
        return true
    }

    private fun buildMapView() {
        requestPermissionsIfNecessary(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        ))

        activity_tracker_mapview.setTileSource(TileSourceFactory.MAPNIK)
        activity_tracker_mapview.setMultiTouchControls(true)
        mapController = activity_tracker_mapview.controller
        mapController.setZoom(3.0)
        val mLocationOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(this),
            activity_tracker_mapview
        )
        mLocationOverlay.enableMyLocation()
        activity_tracker_mapview.overlays.add(mLocationOverlay)

        val mCompassOverlay = CompassOverlay(
            this,
            InternalCompassOrientationProvider(this),
            activity_tracker_mapview
        )

        mCompassOverlay.enableCompass()

        activity_tracker_mapview.overlays.apply {
            add(mLocationOverlay)
            add(mCompassOverlay)
        }
    }
}
