package com.kta.app.data.respone

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("no_hp")
    val no_hp: String,
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("no_hp")
    val no_hp: String,
    @SerializedName("id_anggota")
    val id_anggota: String,
)