package com.kta.app.data.respone

import com.google.gson.annotations.SerializedName

data class TodayResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<TodayItem>
)

data class TodayItem(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("nama_kegiatan")
    val name: String,
    @field:SerializedName("tanggal")
    val date: String,
    @field:SerializedName("jam")
    val time: String,
    @field:SerializedName("lokasi")
    val location: String,
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("absensi")
    val absent: String,
    @field:SerializedName("pic")
    val pic: String,
    @field:SerializedName("notulensi")
    val note: String
)
