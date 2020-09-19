package com.example.simplegpstracker.ui

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.simplegpstracker.R
import com.example.simplegpstracker.model.Constants


class TrackerService: Service() {

    var isRecording = false

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        isRecording = intent.getBooleanExtra(Constants.Intent.IS_RECORDING_EXTRA, false)

        val notification = NotificationCompat.Builder(this, Constants.Notification.CHANNEL_ID)
            .setContentTitle(getString(R.string.recording_notification_title))
            .setContentText(getString(R.string.recording_notification_content_text))
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
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}