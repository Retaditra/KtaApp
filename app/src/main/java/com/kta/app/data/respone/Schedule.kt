package com.kta.app.data.respone

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<ItemSchedule>
)

data class ItemSchedule(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama_kegiatan")
    val nameSchedule: String,
    @SerializedName("lokasi")
    val location: String,
    @SerializedName("jam")
    val time: String,
    @SerializedName("tanggal")
    val date: String,
    @SerializedName("absensi")
    val absent: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("notulensi")
    val notulen: String
)