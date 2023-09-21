package com.kta.app.data.database

import android.content.Context
import com.kta.app.utils.executeThread

class DataRepository(private val dao: ScheduleDao) {

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
        private var instance: DataRepository? = null
        fun getInstance(context: Context): DataRepository {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = ScheduleDatabase.getInstance(context)
                    instance = DataRepository(database.scheduleDao())
                }
                return instance as DataRepository
            }
        }
    }
}

/*
   fun getNearestSchedule(queryType: QueryType): LiveData<ScheduleEntity?> {
        val query = QueryUtil.nearestQuery(queryType)
        return dao.getNearestSchedule(query)
    }
 */