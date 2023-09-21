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
    ) {
        val request = LoginRequest(phone, password)
        val call = ApiConfig().getApi().loginUser(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.token

                    if (token != null) {
                        val userPhone = response.body()?.no_hp
                        val userName = response.body()?.nama
                        val userId = response.body()?.id_anggota
                        EncryptPreferences(getApplication()).savePreferences(
                            token = token,
                            name = userName.toString(),
                            phone = userPhone.toString(),
                            id = userId.toString(),
                        )
                        onSuccess()

                        val success = response.body()?.message
                        message(success.toString())
                    }

                } else {
                    message(getApplication<Application>().getString(R.string.failedLogin))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                message(getApplication<Application>().getString(R.string.failure))
            }
        })
    }
}
