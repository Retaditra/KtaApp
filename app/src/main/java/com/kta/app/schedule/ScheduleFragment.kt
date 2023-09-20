package com.kta.app.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kta.app.R
import com.kta.app.SessionExpiredDialog
import com.kta.app.data.Schedule
import com.kta.app.databinding.FragmentScheduleBinding
import com.kta.app.utils.EncryptedSharedPreferences

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleAdapter
    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var sharedPreferencesHelper: EncryptedSharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesHelper = EncryptedSharedPreferences(requireContext())

        recyclerView = binding.rvSchedule
        adapter = ScheduleAdapter(
            onClick = { showScheduleDetail(it) },
            absent = { absentButton(it) })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        binding.refresh.setOnClickListener {
            getSchedule()
        }

        getSchedule()
    }

    private fun getSchedule() {
        val token = sharedPreferencesHelper.getSharedPreferences().getString("token", null)
        progressBar(true)

        viewModel.getSchedule(token.toString(),
            onSuccess = { list ->
                val pagingData: PagingData<Schedule> = PagingData.from(list)
                adapter.submitData(lifecycle, pagingData)
                progressBar(false)
            },
            onFailure = { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                progressBar(false)
                if (errorMessage == "unauthenticated") {
                    SessionExpiredDialog.show(requireContext())
                }
            })
    }

    private fun progressBar(visible: Boolean) {
        binding.progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showScheduleDetail(schedule: Schedule) {
        val fragment = DetailScheduleFragment.newInstance(schedule)
        val transaction = (requireActivity() as AppCompatActivity)
            .supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun absentButton(schedule: Schedule) {
        val name = schedule.namaKegiatan
        Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show()
    }
}

/*
val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

for (schedule in scheduleList) {
    val timestamp = getTimestampFromString(schedule.tanggal, schedule.waktu)
    if (timestamp != -1L) {
        val intent = Intent(requireContext(), MyAlarmReceiver::class.java)
        intent.putExtra("namaKegiatan", schedule.namaKegiatan)
        intent.putExtra("schedule_id", schedule.id)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            schedule.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            timestamp,
            pendingIntent
        )
    }
}

val pagingData: PagingData<ScheduleList> = PagingData.from(scheduleList)
adapter.submitData(lifecycle, pagingData)
}


private fun getTimestampFromString(date: String, time: String): Long {
try {
    val timeFormatted = time.replace(" WIB", "").trim() // Hapus " WIB" dan spasi

    val dateTimeString = "$date $timeFormatted"
    val inputFormat = SimpleDateFormat("EEEE, d MMMM yyyy HH.mm", Locale("id"))
    val outputFormat = SimpleDateFormat("dd/MM/yyyy HH.mm", Locale("id"))

    val dateObj = inputFormat.parse(dateTimeString)

    return if (dateObj != null) {
        val outputDate = outputFormat.format(dateObj)
        val outputDateObj = outputFormat.parse(outputDate)
        outputDateObj?.time ?: -1L
    } else {
        -1L
    }
} catch (e: Exception) {
    e.printStackTrace()
}
return -1L
}
 */
