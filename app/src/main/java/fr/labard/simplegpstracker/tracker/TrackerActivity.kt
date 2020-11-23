package fr.labard.simplegpstracker.tracker

import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.GPSApplication
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.util.Constants
import kotlinx.android.synthetic.main.activity_tracker.*

class TrackerActivity : AppCompatActivity() {

    private lateinit var viewModel: TrackerActivityViewModel

    private lateinit var gpsService: GpsService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GpsService.LocalBinder
            gpsService = binder.getService()
            viewModel.serviceIsBound = true

            gpsService.activeRecordId = intent.getStringExtra(Constants.Intent.RECORD_ID_EXTRA)!!
            viewModel.activeRecordId = gpsService.activeRecordId
            gpsService.lastLocation.observe(this@TrackerActivity, {location ->
                if (gpsService.gpsMode.value == Constants.Service.MODE_RECORD) {
                    location?.let { viewModel.insertLocation(it) }
                }
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            viewModel.serviceIsBound = false
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

        viewModel = ViewModelProvider(
            this, TrackerActivityViewModelFactory(
                (applicationContext as GPSApplication).appRepository
            )).get(TrackerActivityViewModel::class.java)

        Intent(this, GpsService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        // load the map fragment in the fragment container
        if (findViewById<FrameLayout>(R.id.activity_tracker_fragment_container) != null) {
            if (savedInstanceState != null) return
            supportFragmentManager.commit {
                add<MapFragment>(R.id.activity_tracker_fragment_container, null, intent.extras)
            }
        }

        viewModel.allRecords.observe(this, {
            viewModel.activeRecordId?.let {
                activity_tracker_toolbar.title = viewModel.getRecordById(it).name
            }
        })

        viewModel.allLocations.observe(this, {})
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        viewModel.serviceIsBound = false
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_tracker_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.activity_tracker_action_record_mode -> {
                supportFragmentManager.commit {
                    replace<MapFragment>(R.id.activity_tracker_fragment_container, null, bundleOf())
                    addToBackStack(null)
                }
            }
            R.id.activity_tracker_action_follow_mode -> {
                supportFragmentManager.commit {
                    replace<FollowFragment>(R.id.activity_tracker_fragment_container, null, bundleOf())
                    addToBackStack(null)
                }
            }
            R.id.activity_tracker_action_stats -> {
                val dialogFragment = StatisticsFragment()
                dialogFragment.show(supportFragmentManager, getString(R.string.stats_fragment_tag))
            }
            R.id.activity_tracker_action_rename -> {
                val dialogRename = layoutInflater.inflate(R.layout.dialog_rename, null)
                AlertDialog.Builder(this)
                    .setView(dialogRename)
                    .setTitle(R.string.rename)
                    .setMessage(R.string.rename_message)
                    .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
                        val renameEdittext = dialogRename.findViewById<EditText>(R.id.rename_edittext)
                        val name = renameEdittext.text.toString()
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
            }
            R.id.activity_tracker_action_delete -> {
                // display a confirmation message to delete the record
                AlertDialog.Builder(this)
                    .setTitle(R.string.delete)
                    .setMessage(R.string.delete_message)
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        viewModel.deleteRecord(viewModel.activeRecordId!!)
                        Toast.makeText(this, R.string.deletion_complete, Toast.LENGTH_LONG)
                        finish()
                    }
                    .setNegativeButton(R.string.no) { _: DialogInterface, _: Int ->
                        Toast.makeText(this, R.string.deletion_canceled, Toast.LENGTH_LONG).show()
                    }
                    .show()
            }
            R.id.activity_tracker_action_share_position -> {
                val location = viewModel.allLocations.value
                    ?.last { l -> l.time == viewModel.allLocations.value?.maxOf { it.time }}
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_position))
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.find_me_here).format(location?.latitude, location?.longitude))
                }
                startActivity(intent)
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
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
                Constants.Intent.REQUEST_CODE
            )
        }
    }
}
