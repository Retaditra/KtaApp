package com.kta.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kta.app.data.ScheduleList
import com.kta.app.schedule.ScheduleFragment

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "ACTION_NOTIFICATION") {
            val scheduleList = intent.getParcelableExtra("scheduleList") as ScheduleList?

            if (scheduleList != null) {
                showNotification(context, scheduleList)
            }
        }
    }

    private fun showNotification(context: Context?, scheduleList: ScheduleList) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "your_notification_channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Your Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, ScheduleFragment::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(scheduleList.namaKegiatan)
            .setContentText("Waktu: ${scheduleList.waktu}, Tanggal: ${scheduleList.tanggal}")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notification = builder.build()
        notificationManager.notify(scheduleList.id, notification)
    }
}

