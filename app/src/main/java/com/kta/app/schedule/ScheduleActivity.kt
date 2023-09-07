package com.kta.app.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kta.app.data.ScheduleList
import com.kta.app.databinding.ActivityScheduleBinding
import com.kta.app.utils.CardList
import com.kta.app.utils.DummyDataHelper
import org.json.JSONArray

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rvSchedule //
        adapter = ScheduleAdapter {
            // Handle klik item di sini (jika diperlukan)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val spacing = CardList(20)
        binding.rvSchedule.addItemDecoration(spacing)

        // Load data dummy dan berikan ke adapter
        val dummyData = DummyDataHelper.loadDummyData(this)
        val scheduleList = parseDummyData(dummyData)

        val pagingData: PagingData<ScheduleList> = PagingData.from(scheduleList)
        adapter.submitData(lifecycle, pagingData)

        binding.backButton.setOnClickListener {
            finish()
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
