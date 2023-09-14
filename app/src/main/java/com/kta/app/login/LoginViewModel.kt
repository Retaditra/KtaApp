package com.kta.app.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kta.app.utils.EncryptedSharedPreferences

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferencesHelper: EncryptedSharedPreferences

    init {
        sharedPreferencesHelper = EncryptedSharedPreferences(application)
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        /*
        val request = LoginRequest(email, password)
        val call = ApiConfig().getApi().loginUser(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.token

                    if (token != null) {
                        val userEmail = response.body()?.email
                        val userName = response.body()?.name
                        saveUserCredentials(token, userEmail, userName)
                        onSuccess()
                    }

                } else {
                    val errorResponse = response.errorBody()?.string()
                    val error = Gson().fromJson(errorResponse, LoginErrorResponse::class.java)
                    val errorMessage = error.message
                    onFailure(errorMessage)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onFailure(getApplication<Application>().getString(R.string.failServer))
            }
        })
         */
    }

    fun saveUserCredentials(token: String, id: String, name: String, email: String) {
        with(sharedPreferencesHelper.getSharedPreferences().edit()) {
            putString("token", token)
            putString("id", id)
            putString("name", name)
            putString("email", email)
            apply()
        }
    }
}
