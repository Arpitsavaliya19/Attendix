package com.arpit.attendixapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.arpit.attendixapp.R

object NotificationHelper {
    private const val CHANNEL_ID = "attendance_alerts"
    private const val CHANNEL_NAME = "Attendance Alerts"
    private const val CHANNEL_DESC = "Notifications for low attendance"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showLowAttendanceNotification(context: Context, subjectName: String, percentage: Float) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Low Attendance Alert")
            .setContentText("Your attendance in $subjectName is only ${"%.1f".format(percentage)}%. Please attend more classes!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(subjectName.hashCode(), builder.build())
    }
}
