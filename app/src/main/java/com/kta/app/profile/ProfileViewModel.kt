package com.kta.app.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kta.app.R
import com.kta.app.data.ApiConfig
import com.kta.app.data.respone.MessageResponse
import com.kta.app.utils.parseError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    fun logout(
        token: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
        loading: (Boolean) -> Unit
    ) {
        loading(true)
        val apiService = ApiConfig().getApi()
        val call = apiService.logout("Bearer $token")

        call.enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    message?.let { onSuccess(it) }
                    loading(false)
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

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                onFailure(getApplication<Application>().getString(R.string.failure))
                loading(false)
            }
        })
    }
}