package com.kta.app.data.respone

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ProfileItem
)

data class ProfileItem(
    @field:SerializedName("nama")
    val name: String,
    @field:SerializedName("no_anggota")
    val id: String,
    @field:SerializedName("jabatan")
    val role: String,
    @field:SerializedName("no_hp")
    val phone: String,
    @field:SerializedName("tempat_lahir")
    val birthplace: String,
    @field:SerializedName("tgl_lahir")
    val dateBirth: String,
    @field:SerializedName("foto_profil")
    val imageProfile: String,
    @field:SerializedName("ktaUrl")
    val ktaUrl: String
)
