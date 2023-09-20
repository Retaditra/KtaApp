package com.kta.app.utils

import com.kta.app.data.Schedule
import com.kta.app.data.respone.ItemSchedule
import java.text.SimpleDateFormat
import java.util.*

fun responseToSchedule(responseSchedule: List<ItemSchedule>): List<Schedule> {
    return responseSchedule.map {
        val status = getStatusDescription(it.status.toInt())
        val date = formatDate(it.date)
        Schedule(
            it.id,
            it.nameSchedule,
            it.location,
            it.time,
            date,
            it.absent,
            status,
            it.pic,
            it.notulen
        )
    }
}

private fun getStatusDescription(statusCode: Int): String {
    return when (statusCode) {
        0 -> "Akan Datang"
        1 -> "Berlangsung"
        2 -> "Sudah Selesai"
        else -> "-"
    }
}

private fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date: Date = inputFormat.parse(inputDate) ?: Date()
    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    return outputFormat.format(date)
}
