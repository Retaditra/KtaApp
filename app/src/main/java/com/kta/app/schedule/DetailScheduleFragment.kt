package com.kta.app.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kta.app.databinding.FragmentDetailScheduleBinding

class DetailScheduleFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailScheduleBinding.inflate(inflater, container, false)

        binding.background.setOnClickListener {
            dismiss()
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}
