package com.kta.app.fitur

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kta.app.databinding.FragmentDetailBinding

class DetailFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.background.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}
