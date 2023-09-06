package com.kta.app.fitur

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kta.app.databinding.ActivityAbsentBinding

class AbsentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAbsentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}