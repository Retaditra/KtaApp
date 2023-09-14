package com.kta.app.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.kta.app.NotificationWorker
import com.kta.app.data.ScheduleList
import com.kta.app.databinding.FragmentScheduleBinding
import com.kta.app.utils.DummyDataHelper
import org.json.JSONArray

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.rvSchedule //
        adapter = ScheduleAdapter {
            // Handle klik item di sini (jika diperlukan)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        //val spacing = CardList(20)
        //binding.rvSchedule.addItemDecoration(spacing)

        val dummyData = DummyDataHelper.loadDummyData(requireContext())
        val scheduleList = parseDummyData(dummyData)

        val pagingData: PagingData<ScheduleList> = PagingData.from(scheduleList)
        adapter.submitData(lifecycle, pagingData)

        for (schedule in scheduleList) {
            val inputData = Data.Builder()
                .putInt("id", schedule.id)
                .putString("namaKegiatan", schedule.namaKegiatan)
                .putString("waktu", schedule.waktu)
                .putString("tanggal", schedule.tanggal)
                .putString("lokasi", schedule.lokasi)
                .putString("status", schedule.status)
                .putString("PIC", schedule.pic)
                .putString("aksi", schedule.aksi)
                .putString("notulensi", schedule.notulensi)
                .build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(inputData)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(requireContext()).enqueue(workRequest)
        }
    }

    private fun parseDummyData(dummyData: JSONArray): List<ScheduleList> {
        val scheduleList = mutableListOf<ScheduleList>()
        for (i in 0 until dummyData.length()) {
            val jsonObject = dummyData.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val namaKegiatan = jsonObject.getString("nama_kegiatan")
            val waktu = jsonObject.getString("waktu")
            val tanggal = jsonObject.getString("tanggal")
            val lokasi = jsonObject.getString("lokasi")
            val status = jsonObject.getString("status")
            val pic = jsonObject.getString("PIC")
            val aksi = jsonObject.getString("aksi")
            val notulensi = jsonObject.getString("notulensi")

            val schedule =
                ScheduleList(id, namaKegiatan, waktu, tanggal, lokasi, status, pic, aksi, notulensi)
            scheduleList.add(schedule)
        }
        return scheduleList
    }
}
