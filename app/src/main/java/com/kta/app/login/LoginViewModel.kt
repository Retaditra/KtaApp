package com.kta.app.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kta.app.R
import com.kta.app.data.ApiConfig
import com.kta.app.data.respone.LoginRequest
import com.kta.app.data.respone.LoginResponse
import com.kta.app.utils.EncryptedSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferencesHelper: EncryptedSharedPreferences

    init {
        sharedPreferencesHelper = EncryptedSharedPreferences(application)
    }

    fun login(
        phone: String,
        password: String,
        onSuccess: () -> Unit,
        message: (String) -> Unit,
        onFailure: (String) -> Unit
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
                        saveUserCredentials(
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
                    onFailure(getApplication<Application>().getString(R.string.failedLogin))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onFailure(getApplication<Application>().getString(R.string.failure))
            }
        })
    }

    fun saveUserCredentials(token: String, name: String, phone: String, id: String) {
        with(sharedPreferencesHelper.getSharedPreferences().edit()) {
            putString("token", token)
            putString("name", name)
            putString("phone", phone)
            putString("id", id)
            apply()
        }
    }
}
