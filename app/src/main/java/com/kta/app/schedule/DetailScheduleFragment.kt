package com.kta.app.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kta.app.data.Schedule
import com.kta.app.databinding.FragmentDetailScheduleBinding
import com.kta.app.utils.Constant
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
            requireArguments().getParcelable(Constant.KEY_SCHEDULE, Schedule::class.java)
        } else {
            requireArguments().getParcelable(Constant.KEY_SCHEDULE)
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
    }

    private fun setupView() {
        binding.apply {
            detailName.text = scheduleData?.name
            detailDate.text = scheduleData?.date?.let { formatDate(it) }
            detailTime.text = scheduleData?.time?.let { "$it WIB" }
            detailLocation.text = scheduleData?.location
            detailStatus.text = scheduleData?.status?.let { statusDesc(it.toInt()) }
            detailPic.text = scheduleData?.pic
            detailNote.text = scheduleData?.note
        }
    }

    companion object {
        fun newInstance(scheduleData: Schedule): DetailScheduleFragment {
            val fragment = DetailScheduleFragment()
            val args = Bundle()
            args.putParcelable(Constant.KEY_SCHEDULE, scheduleData)
            fragment.arguments = args
            return fragment
        }
    }
}
