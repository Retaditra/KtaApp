package com.kta.app.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.kta.app.R
import com.kta.app.data.ApiConfig
import com.kta.app.data.Schedule
import com.kta.app.data.respone.ErrorResponse
import com.kta.app.data.respone.ScheduleResponse
import com.kta.app.utils.responseToSchedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    fun getSchedule(
        token: String,
        onSuccess: (List<Schedule>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val apiService = ApiConfig().getApi()
        val call = apiService.getSchedule("Bearer $token")

        call.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(
                call: Call<ScheduleResponse>,
                response: Response<ScheduleResponse>
            ) {
                if (response.isSuccessful) {
                    val responseSchedule = response.body()?.data
                    if (responseSchedule != null) {
                        val listHistory = responseToSchedule(responseSchedule)
                        onSuccess(listHistory)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        val errorMessage = errorResponse.errors?.message
                        errorMessage?.let { onFailure(it) }
                    } else {
                        onFailure("Unknown error")
                    }

                }
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                onFailure(getApplication<Application>().getString(R.string.failure))
            }
        })
    }
}
