package com.kta.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.kta.app.data.ScheduleList
import java.text.SimpleDateFormat
import java.util.*

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val outputDateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    override fun doWork(): Result {
        val inputData = inputData
        val id = inputData.getInt("id", -1)
        val namaKegiatan = inputData.getString("namaKegiatan")
        val waktu = inputData.getString("waktu")
        val tanggal = inputData.getString("tanggal")
        val lokasi = inputData.getString("lokasi")
        val status = inputData.getString("status")
        val pic = inputData.getString("pic")
        val aksi = inputData.getString("aksi")
        val notulensi = inputData.getString("notulensi")

        if (namaKegiatan.isNullOrEmpty() || waktu.isNullOrEmpty() || tanggal.isNullOrEmpty()) {
            return Result.failure()
        }

        val inputDate = "$tanggal"
        val inputDateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
        val parsedDate = inputDateFormat.parse(inputDate)
        val formattedDate = outputDateFormat.format(parsedDate)

        val scheduleList = ScheduleList(
            id,
            namaKegiatan,
            waktu,
            formattedDate,
            lokasi ?: "",
            status ?: "",
            pic ?: "",
            aksi ?: "",
            notulensi ?: ""
        )

        val prefManager =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify =
            prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        if (shouldNotify) {
            showNotification(scheduleList)
        }
        return Result.success()
    }

    private fun showNotification(scheduleList: ScheduleList) {
        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(applicationContext, NotificationReceiver::class.java)
        notificationIntent.action = "ACTION_NOTIFICATION"
        notificationIntent.putExtra("scheduleList", scheduleList)

        val notificationId = scheduleList.id

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val currentTime = System.currentTimeMillis()
        val inputDate = "${scheduleList.tanggal} ${scheduleList.waktu.replace("WIB", "").trim()}"
        val scheduledTime = outputDateFormat.parse(inputDate)?.time ?: 0
        val timeDifference = scheduledTime - currentTime - (60 * 60 * 1000)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            currentTime + timeDifference,
            pendingIntent
        )
    }
}

