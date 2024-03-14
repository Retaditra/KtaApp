package com.kta.app.data.respone

import com.google.gson.annotations.SerializedName


data class AbsentRequest(
    @SerializedName("id_kegiatan")
    val id_schedule: Int,
)
