package fr.labard.simplegpstracker.ui

import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.model.Constants
import fr.labard.simplegpstracker.model.GpsService
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

class TrackerActivity : AppCompatActivity() {

    private lateinit var mTrackerActivityViewModel: TrackerActivityViewModel
    private lateinit var mapController: IMapController
    private lateinit var localBroadcastManager: LocalBroadcastManager
    lateinit var locationServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)
        setSupportActionBar(findViewById(R.id.activity_tracker_toolbar))
        mTrackerActivityViewModel = ViewModelProvider(this).get(TrackerActivityViewModel::class.java)
        mTrackerActivityViewModel.recordId = intent.getIntExtra(Constants.Intent.RECORD_ID_EXTRA, 0)
        localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)

        Configuration.getInstance().load(applicationContext, getPreferences(Context.MODE_PRIVATE))
        buildMapView()

        mTrackerActivityViewModel.allRecords
            .observe(this,
                {
                activity_tracker_toolbar.title = mTrackerActivityViewModel
                    .getRecordById(mTrackerActivityViewModel.recordId).name
                }
            )
        mTrackerActivityViewModel.allLocations
            .observe(this,
                {
                    activity_tracker_toolbar.subtitle = (mTrackerActivityViewModel
                        .getLocationsByRecordId(mTrackerActivityViewModel.recordId).count().toString())
                    val line = Polyline()
                    var parcours = mTrackerActivityViewModel
                        .getLocationsByRecordId(mTrackerActivityViewModel.recordId).map {
                        GeoPoint(it.latitude, it.longitude)
                    }
                    if (parcours.isEmpty()) parcours = mutableListOf(GeoPoint(0.0, 0.0))
                    line.setPoints(parcours)
                    activity_tracker_mapview.overlayManager.add(line)
                    mapController.setCenter(parcours.last())
                }
            )

        try {
            if (
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_NETWORK_STATE
                    ), Constants.Permission.REQUEST_CODE
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        locationServiceIntent = Intent(applicationContext, GpsService::class.java)
            .putExtra(Constants.Intent.RECORD_ID_EXTRA, mTrackerActivityViewModel.recordId)
            .setAction(Constants.Intent.ACTION_RECORD)

        activity_tracker_pause_resume_button.setOnClickListener {
            if (mTrackerActivityViewModel.isRecording) {
                stopService(locationServiceIntent)
                activity_tracker_pause_resume_button.setImageResource(R.drawable.ic_action_gps_active)
            } else {
                startService(locationServiceIntent)
                activity_tracker_pause_resume_button.setImageResource(R.drawable.ic_action_gps_inactive)
            }
            mTrackerActivityViewModel.isRecording = !mTrackerActivityViewModel.isRecording
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            LocationBroadcastReceiver,
            IntentFilter(Constants.LocationService.LOCATION_BROADCAST)
        )
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
                            mTrackerActivityViewModel.updateRecordName(name)
                        }
                    }
                    .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                        Toast.makeText(this, getString(R.string.canceled), Toast.LENGTH_LONG).show()
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
                        Toast.makeText(this, R.string.deletion_complete, Toast.LENGTH_LONG)

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
                Constants.Permission.REQUEST_CODE
            )
        }
    }

    private val LocationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent){
            val location = Location(Constants.LocationService.LOCATION_PROVIDER).apply {
                latitude = intent.getDoubleExtra(Constants.Intent.LATITUDE_EXTRA, 0.0)
                longitude = intent.getDoubleExtra(Constants.Intent.LONGITUDE_EXTRA, 0.0)
                speed = intent.getFloatExtra(Constants.Intent.SPEED_EXTRA, 0.0f)
            }
            mTrackerActivityViewModel.insertLocation(location)
        }
    }

    private fun buildMapView() {
        requestPermissionsIfNecessary(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
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

    override fun onPause() {
        super.onStop()
        activity_tracker_mapview.onPause()
    }

    override fun onResume() {
        super.onResume()
        activity_tracker_mapview.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(locationServiceIntent)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(LocationBroadcastReceiver)
    }
}
