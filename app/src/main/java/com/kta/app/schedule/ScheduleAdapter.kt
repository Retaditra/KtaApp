package com.kta.app.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kta.app.R
import com.kta.app.data.ScheduleList
import com.kta.app.databinding.ScheduleBinding

class ScheduleAdapter(
    private val onClick: (ScheduleList) -> Unit
) : PagingDataAdapter<ScheduleList, ScheduleAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = getItem(position)
        habit?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: ScheduleList) {
            with(binding) {
                namaKegiatan.text = schedule.namaKegiatan
                tanggal.text = schedule.tanggal
                lokasi.text = schedule.lokasi
                jam.text = schedule.waktu
                status.text = schedule.status

                btnAbsent.setOnClickListener {
                    val name = schedule.namaKegiatan
                    Toast.makeText(binding.root.context, name, Toast.LENGTH_SHORT).show()
                }

                root.setOnClickListener {
                    val fragment = DetailScheduleFragment.newInstance(schedule)
                    val transaction = (binding.root.context as AppCompatActivity)
                        .supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ScheduleList>() {
            override fun areItemsTheSame(oldItem: ScheduleList, newItem: ScheduleList): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ScheduleList, newItem: ScheduleList): Boolean {
                return oldItem == newItem
            }
        }
    }
}