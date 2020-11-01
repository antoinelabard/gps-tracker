package fr.labard.simplegpstracker.ui.tracker

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
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import fr.labard.simplegpstracker.GPSApplication
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.model.GpsService
import fr.labard.simplegpstracker.model.util.Constants
import fr.labard.simplegpstracker.tracker.TrackerActivityViewModel
import fr.labard.simplegpstracker.tracker.TrackerActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_tracker.*

class TrackerActivity : AppCompatActivity() {

    private lateinit var viewModel: TrackerActivityViewModel
    private lateinit var localBroadcastManager: LocalBroadcastManager
    lateinit var locationServiceIntent: Intent

    private val locationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent){
            val location = Location(Constants.LocationService.LOCATION_PROVIDER).apply {
                latitude = intent.getDoubleExtra(Constants.Intent.LATITUDE_EXTRA, 0.0)
                longitude = intent.getDoubleExtra(Constants.Intent.LONGITUDE_EXTRA, 0.0)
                speed = intent.getFloatExtra(Constants.Intent.SPEED_EXTRA, 0.0f)
                time = intent.getLongExtra(Constants.Intent.TIME_EXTRA, 0)
            }
            viewModel.insertLocation(location)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)
        setSupportActionBar(findViewById(R.id.activity_tracker_toolbar))

        requestPermissionsIfNecessary(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        ))

        viewModel = ViewModelProvider(this, TrackerActivityViewModelFactory(
            (applicationContext as GPSApplication).appRepository
        )
        ).get(TrackerActivityViewModel::class.java)

        viewModel.setActiveRecordId(intent.getStringExtra(Constants.Intent.RECORD_ID_EXTRA)!!)

        MapFragment().arguments = bundleOf(Constants.Intent.RECORD_ID_EXTRA to viewModel.getActiveRecordId())

        localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)

        viewModel.allRecords.observe(this, {
            activity_tracker_toolbar.title = viewModel
                .getRecordById(viewModel.getActiveRecordId()).name
        })

        locationServiceIntent = Intent(applicationContext, GpsService::class.java)
            .putExtra(Constants.Intent.RECORD_ID_EXTRA, viewModel.activeRecordId.value)
            .setAction(Constants.Intent.ACTION_PLAY)

        activity_tracker_play_fab.setOnClickListener {
            if (viewModel.isRecording) {
                stopService(locationServiceIntent)
                activity_tracker_play_fab.setImageResource(R.drawable.ic_action_gps_active)
            } else {
                startService(locationServiceIntent)
                activity_tracker_play_fab.setImageResource(R.drawable.ic_action_gps_inactive)
            }
            viewModel.isRecording = !viewModel.isRecording
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            locationBroadcastReceiver,
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
                            viewModel.updateRecordName(name)
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
                        viewModel.deleteRecord(viewModel.getActiveRecordId())
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setActiveRecordId("")
        stopService(locationServiceIntent)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationBroadcastReceiver)
    }
}