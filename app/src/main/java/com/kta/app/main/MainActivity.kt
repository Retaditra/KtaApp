package com.kta.app.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kta.app.R
import com.kta.app.databinding.ActivityMainBinding
import com.kta.app.login.LoginActivity
import com.kta.app.utils.EncryptedSharedPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferencesHelper: EncryptedSharedPreferences
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var isBackPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = EncryptedSharedPreferences(applicationContext)
        checkTokenAndNavigate()

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    //tab.text = "KTA"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_home_24)
                }
                1 -> {
                    //tab.text = "Schedule"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.list_alt_24)
                }
                2 -> {
                    //tab.text = "Profile"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.account_circle_24)
                }
            }
        }.attach()

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isBackPressedOnce) {
                    finish()
                } else {
                    isBackPressedOnce = true
                    Toast.makeText(
                        this@MainActivity, "Tekan lagi untuk keluar",
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        isBackPressedOnce = false
                    }, 2000)
                }
            }
        })
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