package com.kta.app.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kta.app.R
import com.kta.app.data.ApiConfig
import com.kta.app.data.respone.LoginRequest
import com.kta.app.data.respone.LoginResponse
import com.kta.app.utils.EncryptPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    fun login(
        phone: String,
        password: String,
        onSuccess: () -> Unit,
        message: (String) -> Unit,
        loading: (Boolean) -> Unit
    ) {
        loading(true)
        val request = LoginRequest(phone, password)
        val call = ApiConfig().getApi().loginUser(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.data?.token
                    if (token != null) {
                        EncryptPreferences(getApplication()).savePreferences(token = token)
                        val success = response.body()?.message
                        message(success.toString())
                        onSuccess()
                        loading(false)
                    }

                } else {
                    message(getApplication<Application>().getString(R.string.failedLogin))
                    loading(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                message(getApplication<Application>().getString(R.string.failure))
                loading(false)
            }
        })
    }
}
