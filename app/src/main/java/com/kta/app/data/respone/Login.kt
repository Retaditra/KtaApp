package com.kta.app.data.respone

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("no_hp")
    val no_hp: String,
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: LoginData
)

data class LoginData(
    @SerializedName("token")
    val token: String,
)