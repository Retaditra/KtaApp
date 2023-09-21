package com.kta.app.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.kta.app.R
import com.kta.app.data.ApiConfig
import com.kta.app.data.respone.ErrorResponse
import com.kta.app.data.respone.LogoutResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutViewModel(application: Application) : AndroidViewModel(application) {
    fun logout(
        token: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val apiService = ApiConfig().getApi()
        val call = apiService.logout("Bearer $token")

        call.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    message?.let { onSuccess(it) }

                } else {
                    val error = response.errorBody()?.string()
                    if (error != null) {
                        val errorResponse = Gson().fromJson(error, ErrorResponse::class.java)
                        val errorMessage = errorResponse.errors?.message
                        errorMessage?.let { onFailure(it) }
                    } else {
                        onFailure(getApplication<Application>().getString(R.string.unknownError))
                    }
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                onFailure(getApplication<Application>().getString(R.string.failure))
            }
        })
    }
}