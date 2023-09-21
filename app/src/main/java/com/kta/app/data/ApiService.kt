package com.kta.app.data

import com.kta.app.data.respone.LoginRequest
import com.kta.app.data.respone.LoginResponse
import com.kta.app.data.respone.LogoutResponse
import com.kta.app.data.respone.ScheduleResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface ApiService {
    @POST("/api/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/api/logout")
    fun logout(@Header("Authorization") token: String): Call<LogoutResponse>

    @POST("/api/kegiatan")
    fun getSchedule(@Header("Authorization") token: String): Call<ScheduleResponse>
}
