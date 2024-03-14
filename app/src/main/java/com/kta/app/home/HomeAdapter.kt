package com.kta.app.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kta.app.data.Schedule
import com.kta.app.databinding.TodayScheduleBinding

class HomeAdapter(private val onClick: (Schedule) -> Unit) :
    PagingDataAdapter<Schedule, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            TodayScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val schedule = getItem(position)
            schedule?.let { holder.bind(it) }
        }
    }

    inner class ItemViewHolder(private val binding: TodayScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule) {
            val jamText = "${schedule.time} WIB"
            with(binding) {
                namaKegiatan.text = schedule.name
                location.text = schedule.location
                time.text = jamText
            }
            itemView.setOnClickListener {
                onClick(schedule)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }
        }
    }
}
