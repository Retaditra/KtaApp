package com.kta.app.utils

import com.kta.app.data.Schedule
import com.kta.app.data.database.ScheduleEntity
import com.kta.app.data.respone.ScheduleData
import com.kta.app.data.respone.TodayItem

class DataMapper {
    fun responseToSchedule(itemDataList: List<ScheduleData>): List<Schedule> {
        val scheduleList = mutableListOf<Schedule>()

        for (itemData in itemDataList) {
            val itemSchedule = itemData.itemSchedule
            val schedule = Schedule(
                id_member = itemData.id_member,
                status_absent = itemData.status_absent,
                id = itemSchedule.id,
                name = itemSchedule.name,
                location = itemSchedule.location,
                time = itemSchedule.time,
                date = itemSchedule.date,
                absent = itemSchedule.absent,
                status = itemSchedule.status,
                pic = itemSchedule.pic,
                note = itemSchedule.note
            )
            scheduleList.add(schedule)
        }

        return scheduleList
    }

    fun scheduleToEntity(it: Schedule): ScheduleEntity {
        return ScheduleEntity(
            id_member = it.id_member,
            status_absent = it.status_absent,
            id = it.id,
            name = it.name,
            location = it.location,
            time = it.time,
            date = it.date,
            absent = it.absent,
            status = it.status,
            pic = it.pic,
            note = it.note
        )
    }

    fun entityToSchedule(entity: List<ScheduleEntity>): List<Schedule> {
        return entity.map {
            Schedule(
                id_member = it.id_member,
                status_absent = it.status_absent,
                id = it.id,
                name = it.name,
                location = it.location,
                time = it.time,
                date = it.date,
                absent = it.absent,
                status = it.status,
                pic = it.pic,
                note = it.note
            )
        }
    }

    fun todayItemToSchedule(data: List<TodayItem>): List<Schedule> {
        return data.map {
            Schedule(
                id = it.id,
                name = it.name,
                location = it.location,
                time = it.time,
                date = it.date,
                absent = it.absent,
                status = it.status,
                pic = it.pic,
                note = it.note
            )
        }
    }
}