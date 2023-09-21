package com.kta.app.utils

import com.kta.app.data.Schedule
import com.kta.app.data.database.ScheduleEntity
import com.kta.app.data.respone.ItemSchedule

class DataMapper {
    fun responseToSchedule(responseSchedule: List<ItemSchedule>): List<Schedule> {
        return responseSchedule.map {
            Schedule(
                it.id,
                it.nameSchedule,
                it.location,
                it.time,
                it.date,
                it.absent,
                it.status,
                it.pic,
                it.notulen
            )
        }
    }

    fun scheduleToEntity(schedule: Schedule): ScheduleEntity {
        return ScheduleEntity(
            id = schedule.id,
            nama = schedule.namaKegiatan,
            lokasi = schedule.lokasi,
            waktu = schedule.waktu,
            tanggal = schedule.tanggal,
            aksi = schedule.aksi,
            status = schedule.status,
            pic = schedule.pic,
            notulensi = schedule.notulensi
        )
    }

    fun entityToSchedule(entity: List<ScheduleEntity>): List<Schedule> {
        return entity.map {
            Schedule(
                it.id,
                it.nama,
                it.lokasi,
                it.waktu,
                it.tanggal,
                it.aksi,
                it.status,
                it.pic,
                it.notulensi
            )
        }
    }
}