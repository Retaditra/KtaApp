package com.kta.app.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedule(
    val id: Int,
    val namaKegiatan: String,
    val lokasi: String,
    val waktu: String,
    val tanggal: String,
    val aksi: String,
    val status: String,
    val pic: String,
    val notulensi: String
) : Parcelable



