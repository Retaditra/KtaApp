package com.kta.app.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kta.app.data.ScheduleList
import com.kta.app.databinding.FragmentDetailScheduleBinding

class DetailScheduleFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailScheduleBinding
    private var scheduleData: ScheduleList? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailScheduleBinding.inflate(inflater, container, false)

        scheduleData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable("scheduleData", ScheduleList::class.java)
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable("scheduleData")
        }

        binding.background.setOnClickListener {
            dismiss()
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.detailNama.text = scheduleData?.namaKegiatan
        binding.detailTanggal.text = scheduleData?.tanggal
        binding.detailJam.text = scheduleData?.waktu
        binding.detailLokasi.text = scheduleData?.lokasi
        binding.detailStatus.text = scheduleData?.status
        binding.detailPIC.text = scheduleData?.pic
        binding.detailNotulensi.text = scheduleData?.notulensi

        binding.btnAbsent.setOnClickListener {
            val name = scheduleData?.namaKegiatan
            Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    companion object {
        fun newInstance(scheduleData: ScheduleList): DetailScheduleFragment {
            val fragment = DetailScheduleFragment()
            val args = Bundle()
            args.putParcelable("scheduleData", scheduleData)
            fragment.arguments = args
            return fragment
        }
    }
}
