package com.kta.app.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kta.app.R
import com.kta.app.data.Schedule
import com.kta.app.databinding.ScheduleBinding
import com.kta.app.utils.ButtonUtils
import com.kta.app.utils.formatDate

class ScheduleAdapter(
    private val context: Context,
    private val onClick: (Schedule) -> Unit,
    private val absent: (Schedule) -> Unit,
) : PagingDataAdapter<Schedule, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val typeItem = 0
    private val typeFooter = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            typeItem -> {
                val binding = ScheduleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ItemViewHolder(binding)
            }
            typeFooter -> {
                val footerView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.footer_layout, parent, false)
                FooterViewHolder(footerView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val schedule = getItem(position)
            schedule?.let { holder.bind(it) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            typeFooter
        } else {
            typeItem
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    inner class ItemViewHolder(private val binding: ScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule) {
            val jamText = "${schedule.time} WIB"
            with(binding) {
                namaKegiatan.text = schedule.name
                descDate.text = formatDate(schedule.date)
                descLocation.text = schedule.location
                descTime.text = jamText

                ButtonUtils.setButtonStatus(btnAbsent, schedule)
                ButtonUtils.setButtonTextAndStyle(btnAbsent, schedule)
                ButtonUtils.setButtonBackgroundColor(context, btnAbsent, schedule)

                btnAbsent.setOnClickListener {
                    absent(schedule)
                }

                itemView.setOnClickListener {
                    onClick(schedule)
                }
            }
        }
    }

    inner class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

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
