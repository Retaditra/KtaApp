package com.kta.app.fitur

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kta.app.databinding.ActivityDetailScheduleBinding

class DetailScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}