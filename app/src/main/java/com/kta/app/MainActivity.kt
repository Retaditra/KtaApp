package com.kta.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kta.app.databinding.ActivityMainBinding
import com.kta.app.schedule.DetailScheduleFragment
import com.kta.app.kta.KtaActivity
import com.kta.app.login.LoginActivity
import com.kta.app.schedule.ScheduleActivity
import com.kta.app.utils.EncryptedSharedPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferencesHelper: EncryptedSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = EncryptedSharedPreferences(applicationContext)
        checkTokenAndNavigate()

        binding.ktaDigital.setOnClickListener {
            val intent = Intent(this, KtaActivity::class.java)
            startActivity(intent)
        }

        binding.schedule.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        binding.absent.setOnClickListener {
            Toast.makeText(this, getString(R.string.fiturAbsent), Toast.LENGTH_SHORT).show()
        }

        binding.logout.setOnClickListener {
            logout()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkTokenAndNavigate() {
        val token = sharedPreferencesHelper.getSharedPreferences().getString("token", null)

        if (token == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logout() {
        sharedPreferencesHelper.getSharedPreferences().edit().clear().apply()
    }
}