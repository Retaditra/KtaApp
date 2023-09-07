package com.kta.app.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleList(
    val id: Int,
    val namaKegiatan: String,
    val waktu: String,
    val tanggal: String,
    val lokasi: String,
    val status: String,
    val pic: String,
    val aksi: String,
    val notulensi: String
) : Parcelable



