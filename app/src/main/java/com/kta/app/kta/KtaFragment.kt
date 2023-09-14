package com.kta.app.kta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kta.app.databinding.FragmentKtaBinding

class KtaFragment : Fragment() {

    private lateinit var binding: FragmentKtaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKtaBinding.inflate(inflater, container, false)
        return binding.root
    }
}