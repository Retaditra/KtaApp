package com.kta.app.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kta.app.R
import com.kta.app.data.ApiConfig
import com.kta.app.data.Schedule
import com.kta.app.data.respone.AbsentRequest
import com.kta.app.data.respone.MessageResponse
import com.kta.app.data.respone.ScheduleResponse
import com.kta.app.utils.DataMapper
import com.kta.app.utils.parseError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {
    fun getSchedule(
        token: String,
        onSuccess: (List<Schedule>) -> Unit,
        onFailure: (String) -> Unit,
        loading: (Boolean) -> Unit
    ) {
        loading(true)
        val apiService = ApiConfig().getApi()
        val call = apiService.getSchedule("Bearer $token")

        call.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(
                call: Call<ScheduleResponse>,
                response: Response<ScheduleResponse>
            ) {
                if (response.isSuccessful) {
                    val item = response.body()?.data
                    if (item != null) {
                        val data = DataMapper().responseToSchedule(item)
                        onSuccess(data)
                        loading(false)
                    }
                } else {
                    try {
                        val error = response.errorBody()?.string()
                        onFailure(parseError(error))
                        loading(false)
                    } catch (e: Exception) {
                        val error = response.message()
                        onFailure(error)
                        loading(false)
                    }
                }
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                onFailure(getApplication<Application>().getString(R.string.failure))
                loading(false)
            }
        })
    }

    fun absent(
        token: String,
        id: Int,
        message: (String) -> Unit,
        loading: (Boolean) -> Unit
    ) {
        loading(true)
        val request = AbsentRequest(id)
        val apiService = ApiConfig().getApi()
        val call = apiService.absent("Bearer $token", request)

        call.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.message
                    if (data != null) {
                        message(data)
                        loading(false)
                    }
                } else {
                    try {
                        val error = response.errorBody()?.string()
                        message(parseError(error))
                        loading(false)
                    } catch (e: Exception) {
                        val error = response.message()
                        message(error)
                        loading(false)
                    }
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                message(getApplication<Application>().getString(R.string.failure))
                loading(false)
            }
        })
    }
}
