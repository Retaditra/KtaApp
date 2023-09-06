package com.kta.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kta.app.databinding.ActivityMainBinding
import com.kta.app.fitur.DetailFragment
import com.kta.app.fitur.KtaActivity
import com.kta.app.fitur.ScheduleActivity
import com.kta.app.utils.EncryptedSharedPreferencesHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferencesHelper: EncryptedSharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = EncryptedSharedPreferencesHelper(applicationContext)
        checkTokenAndNavigate()

        binding.ktaDigital.setOnClickListener {
            val intent = Intent(this, KtaActivity::class.java)
            startActivity(intent)
        }

        binding.schedule.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        /*
        binding.absent.setOnClickListener {
            val intent = Intent(this, AbsentActivity::class.java)
            startActivity(intent)
        }
         */
        binding.absent.setOnClickListener {
            val detailFragment = DetailFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
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