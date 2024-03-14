package com.kta.app.data

import com.kta.app.data.respone.*
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface ApiService {
    @POST("/api/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/api/logout")
    fun logout(@Header("Authorization") token: String): Call<MessageResponse>

    @POST("/api/detail-anggota")
    fun getUserProfile(@Header("Authorization") token: String): Call<ProfileResponse>

    @POST("/api/kegiatan")
    fun getSchedule(@Header("Authorization") token: String): Call<ScheduleResponse>

    @POST("/api/kegiatan/hari-ini")
    fun getTodaySchedule(@Header("Authorization") token: String): Call<TodayResponse>

    @POST("/api/kegiatan/absen")
    fun absent(
        @Header("Authorization") token: String, @Body absent: AbsentRequest
    ): Call<MessageResponse>
}
