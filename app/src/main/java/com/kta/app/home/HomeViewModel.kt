package com.kta.app.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.kta.app.R
import com.kta.app.data.ApiConfig
import com.kta.app.data.Schedule
import com.kta.app.data.respone.ProfileItem
import com.kta.app.data.respone.ProfileResponse
import com.kta.app.data.respone.TodayResponse
import com.kta.app.utils.DataMapper
import com.kta.app.utils.UserProfilePreferences
import com.kta.app.utils.parseError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    fun userProfile(
        token: String,
        onSuccess: (ProfileItem) -> Unit,
        onFailure: (String) -> Unit,
        loading: (Boolean) -> Unit
    ) {
        loading(true)
        val call = ApiConfig().getApi().getUserProfile("Bearer $token")
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>, response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val id = response.body()?.data
                    if (id != null) {
                        val data = response.body()?.data
                        UserProfilePreferences.init(getApplication())
                        UserProfilePreferences.saveUserProfile(
                            data?.id.toString(),
                            data?.name.toString(),
                            data?.phone.toString(),
                            data?.dateBirth.toString(),
                            data?.birthplace.toString(),
                            data?.role.toString(),
                            data?.imageProfile.toString()
                        )
                        data?.let { onSuccess(it) }
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
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                onFailure(getApplication<Application>().getString(R.string.failure))
                loading(false)
            }
        })
    }

    fun getTodaySch(
        token: String,
        onSuccess: (List<Schedule>) -> Unit,
        onFailure: (String) -> Unit,
        loading: (Boolean) -> Unit
    ) {
        loading(true)
        val call = ApiConfig().getApi().getTodaySchedule("Bearer $token")
        call.enqueue(object : Callback<TodayResponse> {
            override fun onResponse(
                call: Call<TodayResponse>, response: Response<TodayResponse>
            ) {
                loading(false)
                if (response.isSuccessful) {

                    val item = response.body()?.data
                    if (item != null) {
                        val data = DataMapper().todayItemToSchedule(item)
                        onSuccess(data)
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

            override fun onFailure(call: Call<TodayResponse>, t: Throwable) {
                onFailure(getApplication<Application>().getString(R.string.failure))
                loading(false)
            }
        })
    }
}