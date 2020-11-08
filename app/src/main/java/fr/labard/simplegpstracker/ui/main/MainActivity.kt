package fr.labard.simplegpstracker.ui.main

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.GPSApplication
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.model.main.MainActivityViewModel
import fr.labard.simplegpstracker.model.main.MainActivityViewModelFactory
import fr.labard.simplegpstracker.model.util.Constants
import fr.labard.simplegpstracker.model.util.XmlParser
import java.io.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))
        createNotificationChannel()

        viewModel = ViewModelProvider(this, MainActivityViewModelFactory(
            (applicationContext as GPSApplication).appRepository
        )
        ).get(MainActivityViewModel::class.java)

        viewModel.allRecords.observe(this, {
            viewModel.records = viewModel.allRecords.value!!
        })
        viewModel.allLocations.observe(this, {
            viewModel.locations = viewModel.allLocations.value!!
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.activity_main_action_import -> {
                importData()
            }
            R.id.activity_main_action_export -> {
                exportData()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun exportData() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "backup : ${Date()}.xml")
        }
        startActivityForResult(intent, Constants.Intent.CREATE_FILE_REQUEST_CODE)
    }

    private fun importData() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        startActivityForResult(intent, Constants.Intent.OPEN_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.Intent.CREATE_FILE_REQUEST_CODE ->
                    data?.data?.also { uri ->
                    alterDocument(
                        uri,
                        XmlParser().export(
                            viewModel.allRecords.value!!,
                            viewModel.allLocations.value!!
                        )
                    )
                }
                Constants.Intent.OPEN_FILE_REQUEST_CODE ->
                    data?.data?.also {uri ->
                        val (r, l) = XmlParser().import(ByteArrayInputStream(readTextFromUri(uri).toByteArray()))
                        for (i in r) viewModel.insertRecord(i)
                        for (i in l) viewModel.insertLocation(i)
                    }
            }
        }
    }

    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri): String {
        val stringBuilder = StringBuilder()
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }

    private fun alterDocument(uri: Uri, text: String) {
        val contentResolver = applicationContext.contentResolver
        try {
            contentResolver.openFileDescriptor(uri, "w")?.use {
                FileOutputStream(it.fileDescriptor).use {
                    it.write(text.toByteArray())
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
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
