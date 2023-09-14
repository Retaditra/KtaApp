package com.kta.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kta.app.databinding.ActivityMainBinding
import com.kta.app.login.LoginActivity
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

        val navView: BottomNavigationView = binding.bottomNavigation

        val navController = findNavController(R.id.fragmentContainer)
        navView.setupWithNavController(navController)
    }

    private fun checkTokenAndNavigate() {
        val token = sharedPreferencesHelper.getSharedPreferences().getString("token", null)

        if (token == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}