package com.kta.app.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kta.app.data.Schedule
import com.kta.app.databinding.FragmentDetailScheduleBinding
import com.kta.app.utils.formatDate
import com.kta.app.utils.statusDesc

class DetailScheduleFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailScheduleBinding
    private var scheduleData: Schedule? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailScheduleBinding.inflate(inflater, container, false)

        scheduleData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable("scheduleData", Schedule::class.java)
        } else {
            requireArguments().getParcelable("scheduleData")
        }

        setupView()
        setupButton()

        return binding.root
    }

    private fun setupButton() {
        binding.background.setOnClickListener {
            dismiss()
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.btnAbsent.setOnClickListener {
            val name = scheduleData?.namaKegiatan
            Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupView() {
        binding.apply {
            detailNama.text = scheduleData?.namaKegiatan
            detailTanggal.text = scheduleData?.tanggal?.let { formatDate(it) }
            detailJam.text = scheduleData?.waktu?.let { "$it WIB" }
            detailLokasi.text = scheduleData?.lokasi
            detailStatus.text = scheduleData?.status?.let { statusDesc(it.toInt()) }
            detailPIC.text = scheduleData?.pic
            detailNotulensi.text = scheduleData?.notulensi
        }
    }

    companion object {
        fun newInstance(scheduleData: Schedule): DetailScheduleFragment {
            val fragment = DetailScheduleFragment()
            val args = Bundle()
            args.putParcelable("scheduleData", scheduleData)
            fragment.arguments = args
            return fragment
        }
    }
}
