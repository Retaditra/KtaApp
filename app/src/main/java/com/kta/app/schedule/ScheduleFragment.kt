package com.kta.app.schedule

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.kta.app.data.database.ScheduleEntity
import com.kta.app.data.database.ScheduleRepository
import com.kta.app.databinding.FragmentScheduleBinding
import com.kta.app.utils.Constant
import com.kta.app.utils.DataMapper
import com.kta.app.utils.EncryptPreferences
import com.kta.app.utils.expired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var adapter: ScheduleAdapter
    private lateinit var repository: ScheduleRepository
    private lateinit var recyclerView: RecyclerView
    private val handler = Handler(Looper.getMainLooper())
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
        repository = ScheduleRepository.getInstance(requireContext())
        recyclerView = binding.rvSchedule

        setupRecyclerView()
        getSchedule()
        refresh()
        filter()
    }

    private fun setupRecyclerView() {
        adapter = ScheduleAdapter(
            context = requireContext(),
            onClick = { showScheduleDetail(it) },
            absent = { absentButton(it) })

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ScheduleFragment.adapter
        }
    }

    private fun listToAdapter(schedule: List<ScheduleEntity>) {
        val data = DataMapper().entityToSchedule(schedule)
        val pagingData: PagingData<Schedule> = PagingData.from(data)
        adapter.submitData(lifecycle, pagingData)
    }

    private fun filter() {
        binding.filterRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioAll -> {
                    lifecycleScope.launch {
                        val schedules = withContext(Dispatchers.IO) { repository.getAllSchedule() }
                        listToAdapter(schedules)
                        recyclerView.layoutManager?.scrollToPosition(0)
                    }
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

    private fun refresh() {
        binding.refresh.setOnClickListener {
            binding.refresh.visibility = View.GONE
            getSchedule { success, message ->
                if (success) {
                    Toast.makeText(
                        requireContext(), getString(R.string.refreshSuccess), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
            handler.postDelayed({
                binding.refresh.visibility = View.VISIBLE
            }, 3000.toLong())
        }
    }

    private fun getSchedule(callback: (Boolean, String?) -> Unit = { _, _ -> }) {
        val preference = EncryptPreferences(requireContext())
        val token = preference.getPreferences().getString("token", null)
        binding.radioAll.isChecked = true
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
                callback(true, null)
            },
            onFailure = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                expired(it, requireContext())
                callback(false, it)
            },
            loading = {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            })
    }

    private fun absentButton(schedule: Schedule) {
        val id = schedule.id
        val preference = EncryptPreferences(requireContext())
        val token = preference.getPreferences().getString("token", null)
        viewModel.absent(token.toString(), id,
            message = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                expired(it, requireContext())
                if (it == Constant.ABSENT) {
                    getSchedule()
                }
            },
            loading = {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        )
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
}
