package fr.labard.simplegpstracker.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.model.util.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mMainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
//
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_record_list)
//        val adapter = RecordListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mMainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

//        mMainActivityViewModel.allRecords
//            .observe(this,
//                { records ->
//                    adapter.setRecords(records)
//                })
//
        activity_main_new_record_fab.setOnClickListener {
            val newId = mMainActivityViewModel.generateNewId()
            mMainActivityViewModel.insertNewRecord(newId)
            adapter.notifyDataSetChanged()
            val intent = Intent(this@MainActivity, TrackerActivity::class.java)
            intent.putExtra(Constants.Intent.RECORD_ID_EXTRA, newId)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                Constants.Notification.CHANNEL_ID,
                R.string.app_name.toString(), importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
