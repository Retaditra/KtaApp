package com.kta.app.utils

import java.text.SimpleDateFormat
import java.util.*

fun statusDesc(statusCode: Int): String {
    return when (statusCode) {
        0 -> "Akan Datang"
        1 -> "Berlangsung"
        2 -> "Selesai"
        else -> "-"
    }
}

fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date: Date = inputFormat.parse(inputDate) ?: Date()
    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    return outputFormat.format(date)
}
