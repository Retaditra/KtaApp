package com.kta.app.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kta.app.R
import com.kta.app.data.Schedule
import com.kta.app.data.database.DataRepository
import com.kta.app.data.database.ScheduleEntity
import com.kta.app.databinding.FragmentScheduleBinding
import com.kta.app.utils.DataMapper
import com.kta.app.utils.EncryptPreferences
import com.kta.app.utils.SessionExpiredDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var adapter: ScheduleAdapter
    private lateinit var repository: DataRepository
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = DataRepository.getInstance(requireContext())
        recyclerView = binding.rvSchedule

        binding.refresh.setOnClickListener {
            binding.radioAll.isChecked = true
            getSchedule()
        }

        setupRecyclerView()
        showList()
        filter()
    }

    private fun setupRecyclerView() {
        adapter = ScheduleAdapter(
            onClick = { showScheduleDetail(it) },
            absent = { absentButton(it) })

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ScheduleFragment.adapter
        }
    }

    private fun showList() {
        lifecycleScope.launch {
            val schedules = withContext(Dispatchers.IO) {
                repository.getAllSchedule()
            }
            if (schedules.isNotEmpty()) {
                listToAdapter(schedules)
                recyclerView.layoutManager?.scrollToPosition(0)
            } else {
                getSchedule()
            }
        }
    }

    private fun listToAdapter(schedule: List<ScheduleEntity>) {
        val data = DataMapper().entityToSchedule(schedule)
        val pagingData: PagingData<Schedule> = PagingData.from(data)
        adapter.submitData(lifecycle, pagingData)
    }

    private fun filter() {
        binding.radioAll.isChecked = true
        binding.filterRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioAll -> {
                    showList()
                }
                R.id.radioOn -> {
                    lifecycleScope.launch {
                        val schedules = withContext(Dispatchers.IO) { repository.getScheduleOn() }
                        listToAdapter(schedules)
                        recyclerView.layoutManager?.scrollToPosition(0)
                    }
                }
                R.id.radioComing -> {
                    lifecycleScope.launch {
                        val schedules = withContext(Dispatchers.IO) { repository.getScheduleSoon() }
                        listToAdapter(schedules)
                        recyclerView.layoutManager?.scrollToPosition(0)
                    }
                }
                R.id.radioEnd -> {
                    lifecycleScope.launch {
                        val schedules = withContext(Dispatchers.IO) { repository.getScheduleEnd() }
                        listToAdapter(schedules)
                        recyclerView.layoutManager?.scrollToPosition(0)
                    }
                }
            }
        }
    }

    private fun getSchedule() {
        val preference = EncryptPreferences(requireContext())
        val token = preference.getPreferences().getString("token", null)

        progressBar(true)
        viewModel.getSchedule(token.toString(),
            onSuccess = {
                val pagingData: PagingData<Schedule> = PagingData.from(it)
                adapter.submitData(lifecycle, pagingData)
                recyclerView.layoutManager?.scrollToPosition(0)

                val data = mutableListOf<ScheduleEntity>()
                for (schedule in it) {
                    val mapper = DataMapper().scheduleToEntity(schedule)
                    data.add(mapper)
                }
                lifecycleScope.launch {
                    repository.deleteAll()
                    repository.insertAll(data)
                }
                progressBar(false)
            },
            message = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                progressBar(false)
                if (it == "unauthenticated") {
                    SessionExpiredDialog.show(requireContext())
                }
                progressBar(false)
            })
    }

    private fun absentButton(schedule: Schedule) {
        val name = schedule.namaKegiatan
        Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show()
    }

    private fun showScheduleDetail(schedule: Schedule) {
        val fragment = DetailScheduleFragment.newInstance(schedule)
        (requireActivity() as AppCompatActivity).supportFragmentManager
            .beginTransaction().apply {
                replace(R.id.fragment_container, fragment)
                addToBackStack(null)
                commit()
            }
    }

    private fun progressBar(visible: Boolean) {
        binding.progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
