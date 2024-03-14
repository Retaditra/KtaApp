package com.kta.app.data.database

import android.content.Context
import com.kta.app.utils.executeThread

class ScheduleRepository(private val dao: ScheduleDao) {

    fun getAllSchedule(): List<ScheduleEntity> {
        return dao.getAllSchedule()
    }

    fun getScheduleSoon(): List<ScheduleEntity> {
        return dao.getScheduleSoon()
    }

    fun getScheduleOn(): List<ScheduleEntity> {
        return dao.getScheduleOn()
    }

    fun getScheduleEnd(): List<ScheduleEntity> {
        return dao.getScheduleEnd()
    }

    fun insertAll(schedule: MutableList<ScheduleEntity>) {
        executeThread { dao.insertAll(schedule) }
    }

    fun deleteAll() {
        executeThread { dao.deleteAll() }
    }

    companion object {
        @Volatile
        private var instance: ScheduleRepository? = null
        fun getInstance(context: Context): ScheduleRepository {
            return instance ?: synchronized(ScheduleRepository::class.java) {
                if (instance == null) {
                    val database = ScheduleDatabase.getInstance(context)
                    instance = ScheduleRepository(database.scheduleDao())
                }
                return instance as ScheduleRepository
            }
        }
    }
}
