package com.kta.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kta.app.R
import com.kta.app.data.Schedule
import com.kta.app.schedule.DetailScheduleFragment

class MyAlarmReceiver : BroadcastReceiver() {

    private var scheduleList: List<Schedule>? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val namaKegiatan = intent.getStringExtra("namaKegiatan")

            if (namaKegiatan != null) {
                val scheduleId = intent.getIntExtra("schedule_id", -1)
                if (scheduleId != -1 && scheduleList != null) {
                    val schedule = getScheduleById(scheduleId)
                    if (schedule != null) {
                        showNotification(context, schedule)
                    }
                }
            }
        }
    }

    private fun showNotification(context: Context?, schedule: Schedule) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "kegiatan"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "kegiatan",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, DetailScheduleFragment::class.java)
        notificationIntent.putExtra("schedule_id", schedule.id)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            schedule.id,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(schedule.namaKegiatan)
            .setContentText("Waktu: ${schedule.waktu}, Tanggal: ${schedule.tanggal}")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notification = builder.build()
        notificationManager.notify(schedule.id, notification)
    }

    private fun getScheduleById(scheduleId: Int): Schedule? {
        return scheduleList?.find { it.id == scheduleId }
    }
}

