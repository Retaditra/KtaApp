package com.kta.app.data.respone

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<ScheduleData>
)

data class ScheduleData(
    @SerializedName("id")
    val id_absent: Int,
    @SerializedName("id_kegiatan")
    val id_schedule: Int,
    @SerializedName("id_anggota")
    val id_member: Int,
    @SerializedName("status_absensi")
    val status_absent: String,
    @SerializedName("kegiatan")
    val itemSchedule: ScheduleItem,
)

data class ScheduleItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama_kegiatan")
    val name: String,
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
    val note: String
)