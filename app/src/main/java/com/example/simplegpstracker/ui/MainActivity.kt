package com.example.simplegpstracker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplegpstracker.R
import com.example.simplegpstracker.model.db.record.RecordEntity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

fun toast(context: Context, text: String) = Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

open class MainActivity : AppCompatActivity() {

    private lateinit var mMainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RecordListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mMainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
//        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        mMainActivityViewModel.allRecords
            .observe(this,
                Observer<List<RecordEntity?>?> { records ->
                    adapter.setRecords(records as List<RecordEntity>?)
                })

        activity_new_record_button.setOnClickListener {
            var newId = 0
            val allRecordIds = mMainActivityViewModel.getRecordsIds()
            while (true) {
                if (allRecordIds.find { it == newId } == null) break
                ++newId
            }
            val currentDate = Date()
            mMainActivityViewModel.insertRecord(RecordEntity(newId, "Record $currentDate", currentDate, currentDate))
            adapter.notifyDataSetChanged()
            val intent = Intent(this@MainActivity, TrackerActivity::class.java)
            intent.putExtra("record_id", newId)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
