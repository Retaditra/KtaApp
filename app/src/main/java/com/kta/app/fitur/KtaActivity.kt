package com.kta.app.fitur

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kta.app.databinding.ActivityKtaBinding

class KtaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKtaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKtaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}