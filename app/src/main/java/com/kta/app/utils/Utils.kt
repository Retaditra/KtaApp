package com.kta.app.utils

import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.caverock.androidsvg.SVG
import com.google.gson.Gson
import com.kta.app.data.respone.MessageResponse
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

private val SINGLE_EXECUTOR = Executors.newSingleThreadExecutor()

fun executeThread(f: () -> Unit) {
    SINGLE_EXECUTOR.execute(f)
}

fun parseError(error: String?): String {
    return if (error != null) {
        val errorResponse = Gson().fromJson(error, MessageResponse::class.java)
        (errorResponse.message)
    } else {
        ("Terjadi Kesalahan")
    }
}

fun expired(it: String, context: Context) {
    if (it == Constant.EXPIRED) {
        SessionExpiredDialog.show(context)
    }
}

fun statusDesc(statusCode: Int): String {
    return when (statusCode) {
        0 -> "Akan Datang"
        1 -> "Berlangsung"
        2 -> "Selesai"
        else -> "-"
    }
}

fun formatDate(inputDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date = inputFormat.parse(inputDate) ?: Date()
        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        outputFormat.format(date)
    } catch (e: Exception) {
        inputDate
    }
}


fun loadSvg(url: String, width: Int, height: Int, callback: (PictureDrawable?) -> Unit) {
    Thread {
        try {
            val svg = SVG.getFromInputStream(java.net.URL(url).openStream())
            svg.documentWidth = width.toFloat()
            svg.documentHeight = height.toFloat()

            val pictureDrawable = PictureDrawable(svg.renderToPicture())
            callback(pictureDrawable)
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }.start()
}