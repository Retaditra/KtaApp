package com.kta.app.data.database

import androidx.room.*
import java.util.*

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(schedule: List<ScheduleEntity>)

    @Query("DELETE FROM ScheduleEntity")
    fun deleteAll()

    @Query("SELECT * FROM ScheduleEntity")
    fun getAllSchedule(): List<ScheduleEntity>

    @Query("SELECT * FROM ScheduleEntity WHERE status = 0")
    fun getScheduleSoon(): List<ScheduleEntity>

    @Query("SELECT * FROM ScheduleEntity WHERE status = 1")
    fun getScheduleOn(): List<ScheduleEntity>

    @Query("SELECT * FROM ScheduleEntity WHERE status = 2")
    fun getScheduleEnd(): List<ScheduleEntity>
}

