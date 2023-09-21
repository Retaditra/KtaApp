package com.kta.app.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.kta.app.R
import com.kta.app.data.ApiConfig
import com.kta.app.data.Schedule
import com.kta.app.data.respone.ErrorResponse
import com.kta.app.data.respone.ScheduleResponse
import com.kta.app.utils.DataMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {
    fun getSchedule(
        token: String,
        onSuccess: (List<Schedule>) -> Unit,
        message: (String) -> Unit,
    ) {
        val apiService = ApiConfig().getApi()
        val call = apiService.getSchedule("Bearer $token")

        call.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(
                call: Call<ScheduleResponse>,
                response: Response<ScheduleResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        val schedule = DataMapper().responseToSchedule(data)
                        onSuccess(schedule)
                        message(response.body()?.message.toString())
                    }
                } else {
                    val error = response.errorBody()?.string()
                    if (error != null) {
                        val errorResponse = Gson().fromJson(error, ErrorResponse::class.java)
                        val errorMessage = errorResponse.errors?.message
                        errorMessage?.let { message(it) }
                    } else {
                        message(getApplication<Application>().getString(R.string.unknownError))
                    }
                }
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                message(getApplication<Application>().getString(R.string.failure))
            }
        })
    }
}
